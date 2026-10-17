package com.taller.poo;

public class Principal {

    public static void main(String[] args) {


        Vehiculo[] vehiculos = {
                new Vehiculo("Toyota", "Corolla", 15000.50, 180, true),
                new Vehiculo(),
                new Vehiculo("Renault", "Duster", 42000.00, 170, false)
        };

        Estudiante[] estudiantes = {
                new Estudiante("E001", "Laura Martínez", 4.35, true),
                new Estudiante("E002", "Andrés Rojas", 3.80, true),
                new Estudiante("E003", "Camila Torres", 2.95, false)
        };

        CuentaBancaria cuenta1 = new CuentaBancaria("1001", "Carlos Pérez", 1000.00, 500.00);
        CuentaBancaria cuenta2 = new CuentaBancaria("1002", "Ana Gómez");
        CuentaBancaria cuenta3 = new CuentaBancaria();

        boolean d1 = cuenta1.depositar(300.00);
        boolean r1 = cuenta1.retirar(49.25);
        boolean d2 = cuenta2.depositar(100.00);
        boolean r2 = cuenta2.retirar(50.00);
        boolean r3 = cuenta3.retirar(500.00);   // supera saldo + crédito: debe fallar
        boolean d3 = cuenta3.depositar(-20.00); // monto inválido: debe fallar

        System.out.printf("=== REPORTE DE VEHÍCULOS ===%n");
        for (int i = 0; i < vehiculos.length; i++) {
            Vehiculo v = vehiculos[i];
            System.out.printf("[%d] Marca: %s | Modelo: %s | KM: %.2f | VelMax: %d km/h | Encendido: %b%n",
                    i + 1, v.getMarca(), v.getModelo(), v.getKilometraje(),
                    v.getVelocidadMaxima(), v.isEncendido());
        }

        System.out.printf("%n=== REPORTE DE ESTUDIANTES ===%n");
        for (Estudiante e : estudiantes) {
            System.out.printf("%s%n", e.obtenerReporteAcademicos());
        }

        System.out.printf("%n=== REPORTE DE CUENTAS BANCARIAS ===%n");
        CuentaBancaria[] cuentas = {cuenta1, cuenta2, cuenta3};
        for (CuentaBancaria c : cuentas) {
            System.out.printf("Cuenta: %s | Titular: %s | Saldo Final: %s | Crédito: %s%n",
                    c.getNumeroCuenta(), c.getTitular(),
                    ConversorMoneda.formatearMoneda(c.getSaldo()),
                    ConversorMoneda.formatearMoneda(c.getLimiteCredito()));
        }

        System.out.printf("%n=== RESULTADO DE OPERACIONES ===%n");
        System.out.printf("Depósito 1001: %b | Retiro 1001: %b%n", d1, r1);
        System.out.printf("Depósito 1002: %b | Retiro 1002: %b%n", d2, r2);
        System.out.printf("Retiro 000000 (excede disponible): %b | Depósito negativo: %b%n", r3, d3);

        double euros = ConversorMoneda.convertirDolaresAEuros(250.00, 0.92);
        double invalido = ConversorMoneda.convertirDolaresAEuros(-5.00, 0.92);
        System.out.printf("%n=== CONVERSOR DE MONEDA ===%n");
        System.out.printf("250.00 USD a EUR (tasa 0.92): %s%n", ConversorMoneda.formatearMoneda(euros));
        System.out.printf("Monto inválido: %s%n", ConversorMoneda.formatearMoneda(invalido));
    }
}
