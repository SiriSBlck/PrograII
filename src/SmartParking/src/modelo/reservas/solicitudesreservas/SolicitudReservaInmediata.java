package modelo.reservas.solicitudesreservas;

import java.time.LocalDateTime;

import list.ArrayList;
import modelo.gestoresplazas.GestorLocalidad;
import modelo.gestoresplazas.GestorZona;
import modelo.vehiculos.Vehiculo;

public class SolicitudReservaInmediata extends SolicitudReserva {
	private int radio;
	//private static final float VALOR_PI = 2;	//Es el valor de PI en la distancia Manhattan, aunque no estoy seguro

	public SolicitudReservaInmediata(int i, int j, LocalDateTime tI, LocalDateTime tF, Vehiculo vehiculo, int radio) {
		super(i, j, tI, tF, vehiculo);
		this.radio = radio;
	}
	
	
	@Override
	public boolean esValida(GestorLocalidad gestorLocalidad) {	//Comprueba si el radio es mayor a 0 y si la distancia a las esquinas es menor al radio
		return super.esValida(gestorLocalidad) && radio > 0 && 
				(radio <= gestorLocalidad.getRadioMaxI() - super.getIZona() + gestorLocalidad.getRadioMaxJ() - super.getJZona()
				|| radio <= super.getIZona() + super.getJZona() 
				|| radio <= gestorLocalidad.getRadioMaxI() - super.getIZona() + super.getJZona()
				|| radio <= gestorLocalidad.getRadioMaxJ() - super.getJZona() + super.getIZona());
	}
	
	@Override
	public void gestionarSolicitudReserva(GestorLocalidad gestor) {
		int x;
		int y;
		int a = getIZona();
		int b = getJZona();
		ArrayList <GestorZona> lista = new ArrayList<>();
		super.gestionarSolicitudReserva(gestor);				//Utiliza la función padre, y si no se encuentra un hueco, entra en el if
		if(super.getHueco() == null) {
			for(int radioActual = 1 ; radioActual <= radio && lista.size() == 0; radioActual++) {
				b--;					//Nos posicionamos a la izquierda del último punto y empezamos a recorrer en sentido antihorario
				x = 1;
				y = 1;
				for(int j = 0 ; j < 4 * radioActual; j++) {
					if(gestor.existeZona(a, b) &&
						gestor.getGestorZona(a, b).existeHueco(super.getTInicial(), super.getTFinal())) {
						
						lista.add(lista.size(), gestor.getGestorZona(a,b));	//Comprobamos si existe la zona y luego si existe hueco en esa zona en cuyo caso,
					}											//añadiremos la zona a una arraylist e indicaremos que en este radio hay un hueco
						
					if(j == 2 * radioActual){		//Comprobamos que siguiendo en esa dirreccion seguimos en el radioActual, y si no
						y *= -1;									//es el caso, cambiamos la dirrección a la adecuada
					}
					
					// los cambios de direccion suceden cuando ha recorrido la cantidad de casillas igual al radio
					if(j == radioActual || j == 3 * radioActual) { 
						x *= -1;
					}
					a += x;
					b += y;
				}
				
				if(lista.size() > 0) {			//Para empezar a comparar precios, tomamos el primer gestor como candidato
					a = lista.get(0).getI();
					b = lista.get(0).getJ();
					double menorPrecio = lista.get(0).getPrecio();
					for(int i = 1 ; i < lista.size() ; i++) {
						if(lista.get(i).getPrecio() < menorPrecio) {	//Si existe un gestor con menor precio lo cambiamos, en caso contrario
							a = lista.get(i).getI();					//se mantendra el candidato debido a la prioridad antihoraria
							b = lista.get(i).getJ();
							menorPrecio = lista.get(i).getPrecio();
						}
					}
					super.setGestorZona(gestor.getGestorZona(a, b));
					super.setHueco(gestor.getGestorZona(a, b).reservarHueco(super.getTInicial(), super.getTFinal()));
					} //if
				} //for
			} //if
		} //gestionarSolicitudReserva
	}
