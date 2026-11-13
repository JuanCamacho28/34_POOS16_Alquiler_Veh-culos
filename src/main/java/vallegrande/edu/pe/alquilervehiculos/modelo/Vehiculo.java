package vallegrande.edu.pe.alquilervehiculos.modelo;

public class Vehiculo {
    private int idVehiculo;
    private String placa, marca, modelo, tipo, estado;
    private int anio;
    private double precioDiario;

    public Vehiculo(int idVehiculo, String placa, String marca, String modelo,
                    int anio, String tipo, double precioDiario, String estado) {
        this.idVehiculo = idVehiculo;
        this.placa = placa;
        this.marca = marca;
        this.modelo = modelo;
        this.anio = anio;
        this.tipo = tipo;
        this.precioDiario = precioDiario;
        this.estado = estado;
    }

    public int getIdVehiculo() { return idVehiculo; }
    public String getPlaca() { return placa; }
    public String getMarca() { return marca; }
    public String getModelo() { return modelo; }
    public int getAnio() { return anio; }
    public String getTipo() { return tipo; }
    public double getPrecioDiario() { return precioDiario; }
    public String getEstado() { return estado; }
}