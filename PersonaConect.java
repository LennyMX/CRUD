package com.example.crudd.conect;

import com.example.crudd.Conexion;
import com.example.crudd.modelo.Persona;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PersonaConect {

    // CREATE
    public void insertar(Persona persona) throws SQLException {

        String sql =
                "INSERT INTO Personas (nombre, direccion) VALUES (?, ?)";

        try (Connection conn = Conexion.conectar();
             PreparedStatement stmt =
                     conn.prepareStatement(
                             sql,
                             Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, persona.getNombre());
            stmt.setString(2, persona.getDireccion());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {

                if (rs.next()) {
                    persona.setId(rs.getInt(1));
                }
            }
        }
    }

    // READ
    public List<Persona> obtenerTodos() throws SQLException {

        List<Persona> personas = new ArrayList<>();

        String sql =
                "SELECT * FROM Personas ORDER BY id";

        try (Connection conn = Conexion.conectar();
             PreparedStatement stmt =
                     conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {

                Persona persona = new Persona();

                persona.setId(rs.getInt("id"));
                persona.setNombre(
                        rs.getString("nombre")
                );
                persona.setDireccion(
                        rs.getString("direccion")
                );

                personas.add(persona);
            }
        }

        return personas;
    }

    // UPDATE
    public void actualizar(Persona persona) throws SQLException {

        String sql =
                "UPDATE Personas " +
                        "SET nombre = ?, direccion = ? " +
                        "WHERE id = ?";

        try (Connection conn = Conexion.conectar();
             PreparedStatement stmt =
                     conn.prepareStatement(sql)) {

            stmt.setString(1, persona.getNombre());
            stmt.setString(2, persona.getDireccion());
            stmt.setInt(3, persona.getId());

            stmt.executeUpdate();
        }
    }

    // DELETE
    public void eliminar(int id) throws SQLException {

        String sql =
                "DELETE FROM Personas WHERE id = ?";

        try (Connection conn = Conexion.conectar();
             PreparedStatement stmt =
                     conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            stmt.executeUpdate();
        }
    }
}
