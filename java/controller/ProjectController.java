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
import model.Project;
import util.ConnectionFactory;

/**
 *
 * @author vito
 */
public class ProjectController {

   public void save(Project project) {
      String sql = "INSERT INTO projects (name, description, createdAt, updatedAt)"
              + "VALUES (?, ?, ?, ?)";
      Connection connection = null;
      PreparedStatement statement = null;
      try {
         //estabelecendo conexao com o BD
         connection = ConnectionFactory.getConnection();
         //preparando a query
         statement = connection.prepareStatement(sql);
         //setando valores
         statement.setString(1, project.getName());
         statement.setString(2, project.getDescription());
         statement.setDate(3, new Date(project.getCreatedAt().getTime()));
         statement.setDate(4, new Date(project.getUpdatedAt().getTime()));
         //executando a query
         statement.execute();
      } catch (SQLException e) {
         //tratando excecoes
         throw new RuntimeException("Erro ao salvar projeto" + e.getMessage(), e);
      } finally {
         //finalizando conexao com o BD
         ConnectionFactory.closeConnection(connection, statement);
      }
   }

   public void update(Project project) {
      String sql = "UPDATE projects SET name = ?, "
              + "description = ?,"
              + "createdAt = ?, "
              + "updatedAt = ?"
              + " WHERE id = ?";
      Connection connection = null;
      PreparedStatement statement = null;
      try {
         //Estabelecendo conexao com o BD
         connection = ConnectionFactory.getConnection();
         //Preparando a Query
         statement = connection.prepareStatement(sql);
         //Setando os valores do statement
         statement.setString(1, project.getName());
         statement.setString(2, project.getDescription());
         statement.setDate(3, new Date(project.getCreatedAt().getTime()));
         statement.setDate(4, new Date(project.getUpdatedAt().getTime()));
         statement.setInt(5, project.getId());
         //executando a query
         statement.execute();
      } catch (SQLException e) {
         throw new RuntimeException("Erro ao atualizar a projeto" + e.getMessage(), e);
      } finally {
         ConnectionFactory.closeConnection(connection, statement);
      }
   }

   public void removeById(int projectId) {
      String sql = "DELETE FROM projects WHERE id = ?";
      Connection connection = null;
      PreparedStatement statement = null;
      try {
         //Estabelecendo conexao com o BD
         connection = ConnectionFactory.getConnection();

         //Preparando a query
         statement = connection.prepareStatement(sql);

         //Setando valores
         statement.setInt(1, projectId);

         //Executando a query
         statement.execute();
      } catch (SQLException e) {
         throw new RuntimeException("Erro ao deletar a projeto" + e.getMessage(), e);
      } finally {
         ConnectionFactory.closeConnection(connection, statement);
      }
   }

   public List<Project> getAll() {
      String sql = "SELECT * FROM projects";
      Connection connection = null;
      PreparedStatement statement = null;
      ResultSet resultSet = null;
      List<Project> projects = new ArrayList<Project>();
      try {
         //criando conexao
         connection = ConnectionFactory.getConnection();
         statement = connection.prepareStatement(sql);
         //valor retornado pela execucao da query
         resultSet = statement.executeQuery();

         while (resultSet.next()) {
            Project project = new Project();
            project.setId(resultSet.getInt("id"));
            project.setName(resultSet.getString("name"));
            project.setDescription(resultSet.getString("description"));
            project.setCreatedAt(resultSet.getDate("createdAt"));
            project.setUpdatedAt(resultSet.getDate("updatedAt"));
            projects.add(project);
         }
      } catch (SQLException e) {
         throw new RuntimeException("Erro ao obter listas de projetos" + e.getMessage(), e);
      } finally {
         ConnectionFactory.closeConnection(connection, statement, resultSet);
      }
      return projects;
   }
}
