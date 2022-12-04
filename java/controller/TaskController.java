/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import model.Task;
import util.ConnectionFactory;

/**
 *
 * @author vito
 */
public class TaskController {

   public void save(Task task) {
      String sql = "INSERT INTO tasks (idProject, "
              + "name, "
              + "description,"
              + "completed,"
              + "notes,"
              + "deadline,"
              + "createdAt,"
              + "updatedAt) VALUES(?, ?, ?, ?, ?, ?, ?, ?)";
      Connection connection = null;
      PreparedStatement statement = null;
      try {
         //Estabelecendo conexao com o BD
         connection = ConnectionFactory.getConnection();

         //preparando a query
         statement = connection.prepareStatement(sql);

         //setando valores
         statement.setInt(1, task.getIdProject());
         statement.setString(2, task.getName());
         statement.setString(3, task.getDescription());
         statement.setBoolean(4, task.isIsCompleted());
         statement.setString(5, task.getNotes());
         statement.setDate(6, new Date(task.getDeadline().getTime()));
         statement.setDate(7, new Date(task.getCreatedAt().getTime()));
         statement.setDate(8, new Date(task.getUpdatedAt().getTime()));
         statement.execute();

      } catch (SQLException e) {
         throw new RuntimeException("erro ao salvar tarefa" + e.getMessage(), e);
      } finally {
         ConnectionFactory.closeConnection(connection, statement);
      }
   }

   public void update(Task task) {
      //observar aqui troquei a ordem do _completed_ pelo _notes_ para 
      //a sequencia ficar igual a da funcao salvar
      String sql = "UPDATE tasks SET "
              + "idProject = ?, "
              + "name = ?, "
              + "description = ?, "
              + "completed = ?, "
              + "notes = ?, "
              + "deadline = ?, "
              + "createdAt = ?, "
              + "updatedAt = ? WHERE id = ?";
      Connection connection = null;
      PreparedStatement statement = null;

      try {
         //Estabelecendo conexao com o BD
         connection = ConnectionFactory.getConnection();
         //Preparando a Query
         statement = connection.prepareStatement(sql);
         //Setando os valores do statement

         statement.setInt(1, task.getIdProject());
         statement.setString(2, task.getName());
         statement.setString(3, task.getDescription());
         statement.setBoolean(4, task.isIsCompleted());
         statement.setString(5, task.getNotes());
         statement.setDate(6, new Date(task.getDeadline().getTime()));
         statement.setDate(7, new Date(task.getCreatedAt().getTime()));
         statement.setDate(8, new Date(task.getUpdatedAt().getTime()));
         statement.setInt(9, task.getId());
         statement.execute();
      } catch (SQLException e) {
         throw new RuntimeException("Erro ao atualizar a tarefa" + e.getMessage(), e);
      } finally {
         ConnectionFactory.closeConnection(connection, statement);
      }
   }

   //removendo tarefa a partir do ID
   public void removeById(int taskId){
      String sql = "DELETE FROM tasks WHERE id=?";
      Connection connection = null;
      PreparedStatement statement = null;

      try {
         //Estabelecendo conexao com o BD
         connection = ConnectionFactory.getConnection();

         //Preparando a query
         statement = connection.prepareStatement(sql);

         //Setando valores
         statement.setInt(1, taskId);

         //Executando a query
         statement.execute();
      } catch (SQLException e) {
         throw new RuntimeException("Erro ao deletar a tarefa" + e.getMessage(), e);
      } finally {
         ConnectionFactory.closeConnection(connection, statement);
      }
   }

   public List<Task> getAll(int idProject) {
      String sql = "SELECT * FROM tasks WHERE idProject = ?";
      Connection connection = null;
      PreparedStatement statement = null;
      ResultSet resultSet = null;
      //Lista de tarefas a ser retornada quando o metodo for chamado
      List<Task> tasks = new ArrayList<Task>();

      try {
         //criando conexao
         connection = ConnectionFactory.getConnection();
         statement = connection.prepareStatement(sql);
         //setando o valor que corresponde ao filtro de busca
         statement.setInt(1, idProject);
         //valor retornado pela execucao da query
         resultSet = statement.executeQuery();
         while (resultSet.next()) {
            Task task = new Task();
            task.setId(resultSet.getInt("id"));
            task.setIdProject(resultSet.getInt("idProject"));
            task.setName(resultSet.getString("name"));
            task.setDescription(resultSet.getString("description"));
            task.setNotes(resultSet.getString("notes"));
            task.setIsCompleted(resultSet.getBoolean("completed"));
            task.setDeadline(resultSet.getDate("deadline"));
            task.setCreatedAt(resultSet.getDate("createdAt"));
            task.setUpdatedAt(resultSet.getDate("updatedAt"));
            tasks.add(task);

         }
      } catch (SQLException e) {
         throw new RuntimeException("Erro ao obter listas de tarefas" + e.getMessage(), e);
      } finally {
         ConnectionFactory.closeConnection(connection, statement, resultSet);
      }
      //retornando lista de tarefas criadas a partir do BD
      return tasks;
   }
}
