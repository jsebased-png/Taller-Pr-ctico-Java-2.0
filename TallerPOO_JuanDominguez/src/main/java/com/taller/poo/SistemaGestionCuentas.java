package com.taller.poo;

import java.util.Scanner;

public class SistemaGestionCuentas {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);


        CuentaBancaria[] cuentas = new CuentaBancaria[3];
        cuentas[0] = new CuentaBancaria("1001", "Carlos Pérez", 1000.00, 500.00);
        cuentas[1] = new CuentaBancaria("1002", "Ana Gómez");
        cuentas[2] = new CuentaBancaria("1003", "Luis Ramírez", 250.50, 200.00);

        int opcion = 0;

        do {
            System.out.printf("%n===== SISTEMA DE GESTIÓN DE CUENTAS =====%n");
            System.out.printf("1. Depositar en una cuenta%n");
            System.out.printf("2. Retirar de una cuenta%n");
            System.out.printf("3. Ver reporte consolidado%n");
            System.out.printf("4. Salir%n");
            System.out.printf("Seleccione una opción: ");

            if (scanner.hasNextInt()) {
                opcion = scanner.nextInt();
                scanner.nextLine();
            } else {
                scanner.nextLine();
                System.out.printf("Error: ingrese un número entero válido.%n");
                opcion = 0;
                continue;
            }

            switch (opcion) {
                case 1: {
                    System.out.printf("Número de cuenta: ");
                    String numero = scanner.nextLine().trim();
                    CuentaBancaria cuenta = buscarCuenta(cuentas, numero);
                    if (cuenta == null) {
                        System.out.printf("Error: la cuenta %s no existe.%n", numero);
                        break;
                    }
                    double monto = leerMonto(scanner, "Monto a depositar: ");
                    if (monto > 0) {
                        cuenta.depositar(monto);
                        System.out.printf("Depósito exitoso. Nuevo saldo: %s%n",
                                ConversorMoneda.formatearMoneda(cuenta.getSaldo()));
                    } else {
                        System.out.printf("Error: el monto debe ser estrictamente mayor a 0.%n");
                    }
                    break;
                }
                case 2: {
                    System.out.printf("Número de cuenta: ");
                    String numero = scanner.nextLine().trim();
                    CuentaBancaria cuenta = buscarCuenta(cuentas, numero);
                    if (cuenta == null) {
                        System.out.printf("Error: la cuenta %s no existe.%n", numero);
                        break;
                    }
                    double monto = leerMonto(scanner, "Monto a retirar: ");
                    if (monto <= 0) {
                        System.out.printf("Error: el monto debe ser estrictamente mayor a 0.%n");
                    } else if (cuenta.retirar(monto)) {
                        System.out.printf("Retiro exitoso. Nuevo saldo: %s%n",
                                ConversorMoneda.formatearMoneda(cuenta.getSaldo()));
                    } else {
                        System.out.printf("Error: fondos insuficientes (saldo + crédito).%n");
                    }
                    break;
                }
                case 3:
                    mostrarReporte(cuentas);
                    break;
                case 4:
                    System.out.printf("Saliendo del sistema. ¡Hasta pronto!%n");
                    break;
                default:
                    System.out.printf("Opción inválida: %d. Elija entre 1 y 4.%n", opcion);
                    break;
            }
        } while (opcion != 4);

        scanner.close();
    }

    private static CuentaBancaria buscarCuenta(CuentaBancaria[] cuentas, String numeroCuenta) {
        for (int i = 0; i < cuentas.length; i++) {
            if (cuentas[i].getNumeroCuenta().equals(numeroCuenta)) {
                return cuentas[i];
            }
        }
        return null;
    }

    private static double leerMonto(Scanner scanner, String mensaje) {
        System.out.printf("%s", mensaje);
        if (scanner.hasNextDouble()) {
            double monto = scanner.nextDouble();
            scanner.nextLine();
            return monto;
        }
        scanner.nextLine();
        return -1;
    }

    private static void mostrarReporte(CuentaBancaria[] cuentas) {
        StringBuilder detalle = new StringBuilder();
        double total = 0.0;
        for (int i = 0; i < cuentas.length; i++) {
            CuentaBancaria c = cuentas[i];
            detalle.append(String.format("Cuenta: %s | Titular: %s | Saldo: %s | Crédito: %s%n",
                    c.getNumeroCuenta(), c.getTitular(),
                    ConversorMoneda.formatearMoneda(c.getSaldo()),
                    ConversorMoneda.formatearMoneda(c.getLimiteCredito())));
            total += c.getSaldo();
        }
        System.out.printf("%n=== REPORTE CONSOLIDADO ===%n");
        System.out.printf("%s", detalle);
        System.out.printf("Saldo total acumulado: %s%n", ConversorMoneda.formatearMoneda(total));
    }
}
