import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

public class Solitario {

    private List<Carta> mazo;
    private List<List<Carta>> pilas;

    public Solitario() {
        mazo = new ArrayList<>();
        pilas = new ArrayList<>();
    }

    public void crearMazo() {
        String[] palos = {"Oros", "Copas", "Espadas", "Bastos"};
        int[] valores = {1, 2, 3, 4, 5, 6, 7, 10, 11, 12};
        
        for (String palo : palos) {
            for (int valor : valores) {
                Carta carta = new Carta(palo, valor);
                mazo.add(carta);
            }
        }
    }

    public void mezclarMazo() {
        Collections.shuffle(mazo);
    }

    public void crearPilas() {
        for (int i = 0; i < 7; i++) {
            List<Carta> pila = new ArrayList<>();
            for (int j = 0; j <= i; j++) {
                Carta carta = mazo.remove(0);
                carta.setesVisible(j == i); // La última carta de cada pila es visible
                pila.add(carta);
            }
            pilas.add(pila);
        }
    }

    public void moverCarta(int pilaOrigen, int pilaDestino) {
        List<Carta> pilaO = pilas.get(pilaOrigen);
        List<Carta> pilaD = pilas.get(pilaDestino);
        
        if (pilaO.isEmpty()) {
            System.out.println("La pila de origen está vacía.");
            return;
        }
        
        Carta carta = pilaO.get(pilaO.size() - 1); // Obtener la última carta de la pila de origen
        
        if (!carta.getesVisible()) {
            System.out.println("La carta seleccionada no está visible.");
            return;
        }
        
        if (pilaD.isEmpty()) {
            if (carta.getValor() == 12) { // Solo se puede mover el rey a una pila vacía
                pilaD.add(pilaO.remove(pilaO.size() - 1));
            } else {
                System.out.println("Solo se puede mover un rey a una pila vacía.");
            }
        } else {
            Carta cartaDestino = pilaD.get(pilaD.size() - 1);
            if (carta.getValor() == cartaDestino.getValor() - 1 && !carta.getPalo().equals(cartaDestino.getPalo())) {
                pilaD.add(pilaO.remove(pilaO.size() - 1));
            } else {
                System.out.println("Movimiento no válido.");
            }
        }
    }

    public void aplicarReglas() {
        // Regla 1: Si alguna pila tiene menos de 5 cartas, se revela una nueva carta de la baraja
        for (List<Carta> pila : pilas) {
            if (pila.size() < 5 && !mazo.isEmpty()) {
                Carta carta = mazo.remove(0);
                carta.setesVisible(true);
                pila.add(carta);
            }
        }
    
        // Regla 2: Si alguna pila tiene 0 cartas, se puede mover una K (valor 12) a esa pila
        for (List<Carta> pila : pilas) {
            if (pila.isEmpty()) {
                for (List<Carta> pila2 : pilas) {
                    if (!pila2.isEmpty()) {
                        Carta carta = pila2.get(pila2.size() - 1);
                        if (carta.getValor() == 12) {
                            pila.add(pila2.remove(pila2.size() - 1));
                            break;
                        }
                    }
                }
            }
        }
    
        // Regla 3: Si hay una secuencia descendente de cartas del mismo palo en alguna pila,
        // se puede mover esa secuencia completa a otra pila con una carta de valor superior
        for (List<Carta> pila : pilas) {
            if (!pila.isEmpty()) {
                int inicioSecuencia = -1;
                int finSecuencia = -1;
                int contador = 0;
                for (int i = 0; i < pila.size(); i++) {
                    if (pila.get(i).getesVisible()) {
                        if (inicioSecuencia == -1) {
                            inicioSecuencia = i;
                            finSecuencia = i;
                        } else {
                            if (pila.get(i).getValor() == pila.get(finSecuencia).getValor() - 1 &&
                                    pila.get(i).getPalo().equals(pila.get(finSecuencia).getPalo())) {
                                finSecuencia = i;
                            } else {
                                inicioSecuencia = i;
                                finSecuencia = i;
                            }
                        }
                        contador++;
                    }
                    if (contador == 13) {
                        break;
                    }
                }
    
                if (contador >= 13) {
                    for (List<Carta> pilaDestino : pilas) {
                        if (pilaDestino != pila && !pilaDestino.isEmpty()) {
                            Carta cartaDestino = pilaDestino.get(pilaDestino.size() - 1);
                            if (pila.get(inicioSecuencia).getValor() == cartaDestino.getValor() + 1 &&
                                    pila.get(inicioSecuencia).getPalo().equals(cartaDestino.getPalo())) {
                                for (int i = inicioSecuencia; i <= finSecuencia; i++) {
                                    pilaDestino.add(pila.remove(i));
                                    finSecuencia--;
                                    i--;
                                }
                                break;
                            }
                        }
                    }
                }
            }
        }
    }

    public void jugar() {
        crearMazo();
        mezclarMazo();
        crearPilas();
        aplicarReglas();
    
        Scanner scanner = new Scanner(System.in);
        boolean juegoTerminado = false;
    
        while (!juegoTerminado) {
            imprimirTablero();
    
            System.out.println("Ingrese el número de la pila de origen (1-7) o 0 para salir: ");
            int pilaOrigen = scanner.nextInt();
    
            if (pilaOrigen == 0) {
                juegoTerminado = true;
                continue;
            }
    
            System.out.println("Ingrese el número de la pila de destino (1-7): ");
            int pilaDestino = scanner.nextInt();
    
            moverCarta(pilaOrigen - 1, pilaDestino - 1);
    
            // Verificar si se ha ganado el juego
            juegoTerminado = verificarVictoria();
        }
    
        System.out.println("¡Juego terminado!");
        scanner.close();
    }
    
    public void imprimirTablero() {
        System.out.println("Tablero:");
        for (int i = 0; i < pilas.size(); i++) {
            System.out.print("Pila " + (i + 1) + ": ");
            List<Carta> pila = pilas.get(i);
            for (Carta carta : pila) {
                if (carta.getesVisible()) {
                    System.out.print(carta + " ");
                } else {
                    System.out.print("X ");
                }
            }
            System.out.println();
        }
    }
    
    public boolean verificarVictoria() {
        // Verificar si todas las pilas están vacías
        for (List<Carta> pila : pilas) {
            if (!pila.isEmpty()) {
                return false;
            }
        }
    
        return true;
    }

    
}