package com.example.crudd.conect;
import com.example.crudd.Conexion;
import com.example.crudd.modelo.Telefono;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TelefonoConect {

    // CREATE
    public void insertar(Telefono telefono) throws SQLException {

        String sql =
                "INSERT INTO Telefonos (personaId, telefono) " +
                        "VALUES (?, ?)";

        try (Connection conn = Conexion.conectar();
             PreparedStatement stmt =
                     conn.prepareStatement(
                             sql,
                             Statement.RETURN_GENERATED_KEYS)) {

            stmt.setInt(1, telefono.getPersonaId());
            stmt.setString(2, telefono.getTelefono());

            stmt.executeUpdate();

            try (ResultSet rs = stmt.getGeneratedKeys()) {

                if (rs.next()) {
                    telefono.setId(rs.getInt(1));
                }
            }
        }
    }

    // READ
    public List<Telefono> obtenerPorPersona(
            int personaId) throws SQLException {

        List<Telefono> telefonos = new ArrayList<>();

        String sql =
                "SELECT * FROM Telefonos " +
                        "WHERE personaId = ? " +
                        "ORDER BY id";

        try (Connection conn = Conexion.conectar();
             PreparedStatement stmt =
                     conn.prepareStatement(sql)) {

            stmt.setInt(1, personaId);

            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {

                    Telefono telefono =
                            new Telefono();

                    telefono.setId(
                            rs.getInt("id")
                    );

                    telefono.setPersonaId(
                            rs.getInt("personaId")
                    );

                    telefono.setTelefono(
                            rs.getString("telefono")
                    );

                    telefonos.add(telefono);
                }
            }
        }

        return telefonos;
    }

    // UPDATE
    public void actualizar(
            Telefono telefono) throws SQLException {

        String sql =
                "UPDATE Telefonos " +
                        "SET telefono = ? " +
                        "WHERE id = ?";

        try (Connection conn = Conexion.conectar();
             PreparedStatement stmt =
                     conn.prepareStatement(sql)) {

            stmt.setString(
                    1,
                    telefono.getTelefono()
            );

            stmt.setInt(
                    2,
                    telefono.getId()
            );

            stmt.executeUpdate();
        }
    }

    // DELETE
    public void eliminar(int id) throws SQLException {

        String sql =
                "DELETE FROM Telefonos WHERE id = ?";

        try (Connection conn = Conexion.conectar();
             PreparedStatement stmt =
                     conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            stmt.executeUpdate();
        }
    }
}
