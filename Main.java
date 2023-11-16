/* 
* @author: Felipe Aguilar - 23195
* @date: 13/11/2023
* @version: Laboratorio 4
*/

import java.util.ArrayList;
import java.util.Scanner;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

interface IReserva {
    void registroUsuario(String username, String password, String tipo);

    void cambiarPassword(String nuevaContraseña);

    void cambiarTipoUsuario(String tipo);

    boolean login(String username, String password);

    void reservacion(String fechaVuelo, boolean tipoVuelo, int cantidadBoletos, String aerolinea, Usuario user);

    void confirmacion(String numeroTarjeta, int cuotas, String claseVuelo, String numeroAsiento, int cantidadMaletas);
}

public class Main {
    public static void main(String[] args) {
        Kayak kayak = new Kayak();
        Scanner sc = new Scanner(System.in);
        Usuario iUsuario = null;
        int opcion = 0;
        do {
            System.out.println("Bienvenido a Kayak");
            System.out.println("1. Modo registro");
            System.out.println("2. Ingresar/Salir");
            System.out.println("3. Modo reservas");
            System.out.println("4. Modo confirmación");
            System.out.println("5. Modo perfil");
            System.out.println("0. Salir");
            opcion = sc.nextInt();

            switch (opcion) {
                case 1:
                iUsuario = kayak.registroUsuario();
                    break;

                case 2:
                        kayak.ingresarSalir(iUsuario);
                    break;
                case 3:
                    if (iUsuario == null) {
                        System.out.println("Ingresar sesión antes");
                    } else {
                        kayak.modoReservas(iUsuario);
                    }
                    break;
                case 4:
                    if (iUsuario == null) {
                        System.out.println("Ingresar sesión antes");
                    } else {
                        kayak.modoConfirmacion(iUsuario);
                    }
                    break;
                case 5:
                    if (iUsuario == null) {
                        System.out.println("Ingresar sesión antes");
                    } else {
                        kayak.modoPerfil(iUsuario);
                    }
                    break;
                case 0:
                    System.out.println("Feliz dia!");
                    break;

                default:
                System.out.println("Opcion no valida");
                    break;
            }

    } while (opcion != 0);
}
}

class Kayak {
    private ArrayList<Usuario> usuarios = new ArrayList<>();
    private ArrayList<Reserva> itinerario = new ArrayList<>();

    public Kayak() {
    }
public Usuario  registroUsuario() {
        Scanner sc = new Scanner(System.in);
        System.out.print("Ingrese un nombre de usuario: ");
        String username = sc.next();
        System.out.print("Ingrese una contraseña: ");
        String password = sc.next();
        System.out.print("Seleccione el tipo de usuario (VIP o NoVIP): ");
        String tipo = sc.next();

        for (Usuario usuarioExistente : usuarios) {
            if (usuarioExistente.getUsername().equals(username)) {
            System.out.println("Usuario ya existe. Registro fallido.");
            return null;
            }
        }


        if ("VIP".equalsIgnoreCase(tipo)) {
            VIP vip = new VIP(username, password, tipo);
            usuarios.add(vip);
            escribirUsuarioCSV(username, password, tipo);
            return vip;
        } else if ("NoVIP".equalsIgnoreCase(tipo)) {
            NoVIP noVIP = new NoVIP(username, password, tipo);
            usuarios.add(noVIP);
            escribirUsuarioCSV(username, password, tipo);
            return noVIP;
        } else {
            System.out.println("Tipo de usuario no válido. Registro fallido.");
            return null;
        }
    }

    public void ingresarSalir(IReserva iUsuario) {
        Scanner sc = new Scanner(System.in);
        if (iUsuario == null) {
            System.out.print("Ingrese su nombre de usuario: ");
            String username = sc.next();
            System.out.print("Ingrese su contraseña: ");
            String password = sc.next();

            for (Usuario usuario : usuarios) {
                if (usuario.login(username, password)) {
                    System.out.println("Inicio de sesión exitoso.");
                    return;
                }
            }
            System.out.println("Inicio de sesión fallido. Verifique sus credenciales.");
        } else {
            System.out.println("Cerrando sesión...");
            iUsuario = null;
        }
    }

    public void modoReservas(IReserva iUsuario) {
        if (iUsuario == null) {
            System.out.println("Ingresar sesión antes");
        } else {
            if (iUsuario instanceof VIP) {
                ((VIP) iUsuario).opcionesReservas();
            } else if (iUsuario instanceof NoVIP) {
                ((NoVIP) iUsuario).opcionesReservas();
            } else {
                System.out.println("Tipo de usuario no reconocido");
            }
        }
    }

    public void modoConfirmacion(IReserva iUsuario) {
       if (iUsuario == null) {
            System.out.println("Ingresar sesión antes");
        } else {
            if (iUsuario instanceof VIP) {
                ((VIP) iUsuario).opcionesConfirmacion();
            } else if (iUsuario instanceof NoVIP) {
                ((NoVIP) iUsuario).opcionesConfirmacion();
            } else {
                System.out.println("Tipo de usuario no reconocido");
            }
        }
    }



    public void modoPerfil(IReserva iUsuario) {
        if (iUsuario == null) {
            System.out.println("Ingresar sesión antes");
        } else {
            if (iUsuario instanceof VIP) {
                ((VIP) iUsuario).opcionesPerfil();
            } else if (iUsuario instanceof NoVIP) {
                ((NoVIP) iUsuario).opcionesPerfil();
            } else {
                System.out.println("Tipo de usuario no reconocido");
            }
        }
    }

    private void escribirUsuarioCSV(String username, String password, String tipo) {
    try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("usuarios.csv", true)))) {
        writer.printf("%s,%s,%s%n", username, password, tipo);
        System.out.println("Usuario registrado en usuarios.csv");
    } catch (IOException e) {
        e.printStackTrace();
    }
}

}

class Usuario implements IReserva {
    protected String username;
    protected String password;
    protected String tipo;

    public Usuario(String username, String password, String tipo) {
        this.username = username;
        this.password = password;
        this.tipo = tipo;
    }

    @Override
    public void registroUsuario(String username, String password, String tipo) {
        // Implementa lógica de registro de usuario
        System.out.println("Usuario registrado: " + username);
    }

    @Override
    public void cambiarPassword(String nuevaContraseña) {
        // Implementa lógica de cambio de contraseña
        this.password = nuevaContraseña;
        System.out.println("Contraseña cambiada con éxito.");
    }

    @Override
    public void cambiarTipoUsuario(String tipo) {
        // Implementa lógica de cambio de tipo de usuario
        this.tipo = tipo;
        System.out.println("Tipo de usuario cambiado a: " + tipo);
    }

    @Override
    public boolean login(String username, String password) {
        return this.username.equals(username) && this.password.equals(password);
    }

    @Override
    public void reservacion(String fechaVuelo, boolean tipoVuelo, int cantidadBoletos, String aerolinea, Usuario user) {
        // Implementa lógica de reservación
        System.out.println("Reserva realizada con éxito.");
    }

    @Override
    public void confirmacion(String numeroTarjeta, int cuotas, String claseVuelo, String numeroAsiento, int cantidadMaletas) {
        // Implementa lógica de confirmación
        System.out.println("Confirmación realizada con éxito.");
    }

    public String getUsername() {
    return username;
    }

}

class VIP extends Usuario implements IReserva {
    private Reserva reserva;
    int i;
    Scanner sc = new Scanner(System.in);

    public VIP(String username, String password, String tipo) {
        super(username, password, tipo);
        this.reserva = new Reserva(username);
    }

    public void opcionesReservas() {
        System.out.println("Opciones de Reservas VIP:");
        int opcion = 0;
        do{
            System.out.println("\n1- Definir fecha de viaje.\n2- Definir si es ida y vuelta o solo ida.\n3- Definir cantidad de boletos.\n4- Definir aerolínea. \n0- Salir");
            opcion = sc.nextInt();
            switch (opcion) {
                case 1:
                System.out.println("Ingrese la fecha donde se hara el viaje (DD/MM/AAAA)");
                String fechaVuelo = sc.next();
                sc.nextLine();
                reserva.setFechaVuelo(fechaVuelo);
                    break;
                case 2:
                System.out.println("El vuelo es de ida y regreso (s/n)");
                String idaVuelta = sc.next();
                sc.nextLine();
                if (idaVuelta.toLowerCase().equals("s")) {
                    boolean tipoVuelo = true;
                    reserva.setTipoVuelo(tipoVuelo);
                }else{
                    boolean tipoVuelo = false;
                    reserva.setTipoVuelo(tipoVuelo);
                }
                    break;
            
                case 3:
                System.out.println("Ingrese cantidad de boletos");
                String cantidadBoletos = sc.next();
                sc.nextInt();
                reserva.setCantidadBoletos(Integer.parseInt(cantidadBoletos));
                    break;

                case 4:
                System.out.println("Ingrese la aerolinea");
                String aerolinea = sc.next();
                sc.nextLine();
                reserva.setAerolinea(aerolinea);
                    break;

                case 0:
                System.out.println("Regresando a menu principal");
                    break;

                default:
                System.out.println("Opción no válida");
                    break;
            }
        }while(opcion != 0);
    }

    public void opcionesConfirmacion() {
        System.out.println("Opciones de Confirmación VIP:");
        int opcion = 0;
        do{
            System.out.println("\n1- Definir número de tarjeta.\n2- Imprimir itinerario.\n3- Seleccionar número de asiento.\n4- Definir cantidad de maletas. \n0- Salir");
            opcion = sc.nextInt();
            switch (opcion) {
                case 1:
                System.out.println("Ingrese el numero de la tarjeta");
                String numeroTarjeta = sc.next();
                sc.nextLine();
                reserva.setNumeroTarjeta(numeroTarjeta);
                    break;
                case 2:
                System.out.println("Este es su intinerario");
                reserva.displayReservationDetails();
                    break;
                
                case 3:
                System.out.println("Ingrese su numero de asiento");
                String numeroAsiento = sc.next();
                sc.nextLine();
                reserva.setNumeroAsiento(numeroAsiento);
                    break;

                case 4:
                System.out.println("Ingrese cuantas Maletas utilizara");
                String cantidadMaletas = sc.next();
                sc.nextInt();
                reserva.setCantidadMaletas(Integer.parseInt(cantidadMaletas));
                    break;

                case 0:
                System.out.println("Regresando a menu principal");
                    break;
                default:
                System.out.println("Opción no válida");
                    break;
            }
        }while(opcion != 0);
    }

    public void opcionesPerfil() {
        System.out.println("Opciones de Perfil VIP:");
        int opcion = 0;
        do{
            System.out.println("\n1- Cambiar contraseña.\n0- Salir");
            opcion = sc.nextInt();
            switch (opcion) {
                case 1:
                    System.out.println("Ingresa tu nueva contraseña");
                    String newPassword = sc.next();
                    sc.nextLine();
                    break;

                case 0:
                System.out.println("Regresando a menu principal");
                    break;

                default:
                System.out.println("Opción no válida");
                    break;
            }
        }while(opcion != 0);
    }
}

class NoVIP extends Usuario implements IReserva {
    private Reserva reserva;
    Scanner sc = new Scanner(System.in);

    public NoVIP(String username, String password, String tipo) {
        super(username, password, tipo);
        this.reserva = new Reserva(username);
    }

    public void opcionesReservas() {
        System.out.println("Opciones de Reservas NoVIP:");
        int opcion = 0;
        do{
            System.out.println("\n1- Definir fecha de viaje.\n2- Definir si es ida y vuelta o solo ida.\n3- Definir cantidad de boletos.\n4- Definir aerolínea. \n0- Salir");
            opcion = sc.nextInt();
            switch (opcion) {
                case 1:
                System.out.println("Ingrese la fecha donde se hara el viaje");
                String fechaVuelo = sc.next();
                sc.nextLine();
                reserva.setFechaVuelo(fechaVuelo);
                    break;
                case 2:
                System.out.println("El vuelo es de ida y regreso (s/n)");
                String idaVuelta = sc.next();
                sc.nextLine();
                if (idaVuelta.toLowerCase().equals("s")) {
                    boolean tipoVuelo = true;
                    reserva.setTipoVuelo(tipoVuelo);
                }else{
                    boolean tipoVuelo = false;
                    reserva.setTipoVuelo(tipoVuelo);
                }
                    break;
            
                case 3:
                System.out.println("Ingrese cantidad de boletos");
                String cantidadBoletos = sc.next();
                sc.nextInt();
                reserva.setCantidadBoletos(Integer.parseInt(cantidadBoletos));
                    break;

                case 4:
                System.out.println("Ingrese la aerolinea");
                String aerolinea = sc.next();
                sc.nextLine();
                reserva.setAerolinea(aerolinea);
                    break;

                case 0:
                System.out.println("Regresando a menu principal");
                    break;

                default:
                System.out.println("Opción no válida");
                    break;
            }
        }while(opcion != 0);
    }

    public void opcionesConfirmacion() {
        System.out.println("Opciones de Confirmación NoVIP:");
        int opcion = 0;
        do{
            System.out.println("\n1- Definir número de tarjeta.\n2- Imprimir itinerario.\n3- Definir cantidad de cuotas.\n4- Definir clase para vuelo. \n0- Salir");
            opcion = sc.nextInt();
            switch (opcion) {
                case 1:
                System.out.println("Ingrese el numero de la tarjeta");
                String numeroTarjeta = sc.next();
                sc.nextLine();
                reserva.setNumeroTarjeta(numeroTarjeta);
                    break;
                case 2:
                System.out.println("Este es su intinerario");
                reserva.displayReservationDetails();
                    break;
            
                case 3:
                System.out.println("Ingrese el numero cuotas con el que desea pagar");
                String cuotas = sc.next();
                sc.nextInt();
                reserva.setCuotas(Integer.parseInt(cuotas));
                    break;

                case 4:
                System.out.println("Ingrese tipo de vuelo (Coach o Primera Clase)");
                String claseVuelo = sc.nextLine();
                reserva.setClaseVuelo(claseVuelo);
                    break;

                case 0:
                System.out.println("Regresando a menu principal");
                    break;
                default:
                System.out.println("Opción no válida");
                    break;
            }
        }while(opcion != 0);
    }

    public void opcionesPerfil() {
        System.out.println("Opciones de Perfil NoVIP:");
        int opcion = 0;
        do{
            System.out.println("\n1- Cambiar contraseña.\n2- Modificar el tipo de cliente \n3- Aplicar cupón de 10% de descuento\n0- Salir");
            opcion = sc.nextInt();
            switch (opcion) {
                case 1:
                    System.out.println("Ingresa tu nueva contraseña");
                    String newPassword = sc.next();
                sc.nextLine();
                    break;

                case 0:
                System.out.println("Regresando a menu principal");
                    break;
                default:
                System.out.println("Opción no válida");
                    break;
            }
        }while(opcion != 0);
    }
}

class Reserva {
    private String fechaVuelo;
    private boolean tipoVuelo;
    private int cantidadBoletos;
    private String aerolinea;
    private String username;
    private String numeroTarjeta;
    private int cuotas;
    private String claseVuelo;
    private String numeroAsiento;
    private int cantidadMaletas;

    public Reserva(String username) {
        this.username = username;
    }

    public void setFechaVuelo(String fechaVuelo) {
        this.fechaVuelo = fechaVuelo;
    }

    public void setTipoVuelo(boolean tipoVuelo) {
        this.tipoVuelo = tipoVuelo;
    }

    public void setCantidadBoletos(int cantidadBoletos) {
        this.cantidadBoletos = cantidadBoletos;
    }

    public void setAerolinea(String aerolinea) {
        this.aerolinea = aerolinea;
    }

    public void setNumeroTarjeta(String numeroTarjeta) {
        this.numeroTarjeta = numeroTarjeta;
    }

    public void setCuotas(int cuotas) {
        this.cuotas = cuotas;
    }

    public void setClaseVuelo(String claseVuelo) {
        this.claseVuelo = claseVuelo;
    }
    
    public void setNumeroAsiento(String numeroAsiento) {
        this.numeroAsiento = numeroAsiento;
    }
    
    public void setCantidadMaletas(int cantidadMaletas) {
        this.cantidadMaletas = cantidadMaletas;
    }

    public void displayReservationDetails() {
        try (PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("itinerario.csv", true)))) {
            writer.println("fechaVuelo,tipoVuelo,cantidadBoletos,aerolinea,username,numeroTarjeta,cuotas,claseVuelo,numeroAsiento,cantidadMaletas");
            writer.printf("%s,%s,%d,%s,%s,%s,%d,%s,%s,%d%n",
                    fechaVuelo, tipoVuelo, cantidadBoletos, aerolinea, username, numeroTarjeta, cuotas, claseVuelo, numeroAsiento, cantidadMaletas);
            System.out.println("Data written to itinerario.csv");
        } catch (IOException e) {
            e.printStackTrace();
        }

    } 
}