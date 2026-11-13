package vallegrande.edu.pe.alquilervehiculos.dao;

import vallegrande.edu.pe.alquilervehiculos.modelo.Vehiculo;
import vallegrande.edu.pe.alquilervehiculos.util.Conexion;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class VehiculoDAO {

    public List<Vehiculo> listar() throws SQLException {
        List<Vehiculo> lista = new ArrayList<>();
        String sql = "SELECT id_vehiculo, placa, marca, modelo, `año`, tipo, precio_diario, estado FROM vehiculos";
        try (Connection cn = Conexion.getConnection();
             Statement st = cn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                lista.add(new Vehiculo(
                        rs.getInt("id_vehiculo"), rs.getString("placa"),
                        rs.getString("marca"), rs.getString("modelo"),
                        rs.getInt("año"), rs.getString("tipo"),
                        rs.getDouble("precio_diario"), rs.getString("estado")));
            }
        }
        return lista;
    }

    public void insertar(Vehiculo v) throws SQLException {
        String sql = "INSERT INTO vehiculos (placa, marca, modelo, `año`, tipo, precio_diario, estado) VALUES (?,?,?,?,?,?,?)";
        try (Connection cn = Conexion.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, v.getPlaca());
            ps.setString(2, v.getMarca());
            ps.setString(3, v.getModelo());
            ps.setInt(4, v.getAnio());
            ps.setString(5, v.getTipo());
            ps.setDouble(6, v.getPrecioDiario());
            ps.setString(7, v.getEstado());
            ps.executeUpdate();
        }
    }

    public void actualizar(Vehiculo v) throws SQLException {
        String sql = "UPDATE vehiculos SET placa=?, marca=?, modelo=?, `año`=?, tipo=?, precio_diario=?, estado=? WHERE id_vehiculo=?";
        try (Connection cn = Conexion.getConnection();
             PreparedStatement ps = cn.prepareStatement(sql)) {
            ps.setString(1, v.getPlaca());
            ps.setString(2, v.getMarca());
            ps.setString(3, v.getModelo());
            ps.setInt(4, v.getAnio());
            ps.setString(5, v.getTipo());
            ps.setDouble(6, v.getPrecioDiario());
            ps.setString(7, v.getEstado());
            ps.setInt(8, v.getIdVehiculo());
            ps.executeUpdate();
        }
    }

    public void eliminar(int id) throws SQLException {
        try (Connection cn = Conexion.getConnection();
             PreparedStatement ps = cn.prepareStatement("DELETE FROM vehiculos WHERE id_vehiculo=?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}