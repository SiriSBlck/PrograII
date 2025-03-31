package controladores;

import controladores.excepciones.PlazaOcupada;
import controladores.excepciones.ReservaInvalida;
import controladores.excepciones.SolicitudReservaInvalida;
import list.ArrayList;
import list.IList;
import modelo.gestoresplazas.GestorLocalidad;
import modelo.reservas.EstadoValidez;
import modelo.reservas.Reserva;
import modelo.reservas.Reservas;
import modelo.reservas.solicitudesreservas.SolicitudReserva;
import modelo.reservas.solicitudesreservas.SolicitudReservaAnticipada;
import modelo.vehiculos.Vehiculo;
import anotacion.Programacion2;

@Programacion2 (
		nombreAutor1 = "Daniel",
		apellidoAutor1 = "Czepiel",
		emailUPMAutor1 = "daniel.czepiel@alumnos.upm.es",

		nombreAutor2 = "Clara",
		apellidoAutor2 = "Laguna",
		emailUPMAutor2 = "clara.laguna@alumnos.upm.es"
		)

public class ControladorReservas {
	private Reservas registroReservas;
	private GestorLocalidad gestorLocalidad;

	public GestorLocalidad getGestorLocalidad() {
		return gestorLocalidad;
	}

	public Reservas getRegistroReservas() {
		return registroReservas;
	}

	public boolean esValidaReserva(int i, int j, int numPlaza, int numReserva, String noMatricula) {
		Reserva reserva = this.registroReservas.obtenerReserva(numReserva);
		if (reserva == null) {
			return false;
		}
		reserva.validar(i, j, numPlaza, noMatricula, this.gestorLocalidad);
		return reserva.getEstadoValidez() == EstadoValidez.OK;
	}

	//TO-DO alumno obligatorio

	public ControladorReservas(int[][] plazas, double[][] precios) {
		this.gestorLocalidad = new GestorLocalidad (plazas, precios);
		this.registroReservas = new Reservas();
	}


	//PRE: la solicitud es válida
	public int hacerReserva(SolicitudReserva solicitud) throws SolicitudReservaInvalida {
		if(!solicitud.esValida(gestorLocalidad)) {
			throw new SolicitudReservaInvalida ("La reserva no es valida");
		}
		
		solicitud.gestionarSolicitudReserva(gestorLocalidad);	
		
		if(solicitud.getHueco() != null) {
			return registroReservas.registrarReserva(solicitud);
		}
		
		return -1; //no es necesario emplear el bloque else ya que aumenta la complejidad
	}

	
	public Reserva getReserva(int numReserva) {	
		return registroReservas.obtenerReserva(numReserva);
	}

	
	//PRE: la plaza dada está libre y la reserva está validada			
	public void ocuparPlaza(int i, int j, int numPlaza, int numReserva, Vehiculo vehiculo) throws PlazaOcupada, ReservaInvalida {
		
		registroReservas.obtenerReserva(numReserva).validar(i, j, numPlaza, vehiculo.getMatricula(), gestorLocalidad);
		
		if(registroReservas.obtenerReserva(numReserva).getHueco().getPlaza().getVehiculo() != null) {
			throw new PlazaOcupada ("No ha sido posible llevar a cabo su petición. La plaza solicitada ya está ocupada");
		}
		
		if(registroReservas.obtenerReserva(numReserva).getEstadoValidez() != EstadoValidez.OK) {
			throw new ReservaInvalida ("No ha sido posible llevar a cabo su petición. Su reserva no es valida");
		}
		
		registroReservas.obtenerReserva(numReserva).getHueco().getPlaza().setVehiculo(vehiculo);
	}


	public void desocuparPlaza(int numReserva) {		
		Reserva reservaAux = this.getReserva(numReserva);
		reservaAux.liberarHuecoReservado(); //libera el hueco de la reserva en el gestor
		reservaAux.getGestorZona().liberarHueco(getReserva(numReserva).getHueco()); //registra el hueco dado como disponible para otra reserva
		reservaAux.getHueco().getPlaza().setVehiculo(null);
	}

	
	public void anularReserva(int numReserva) {
		desocuparPlaza(numReserva);
		this.registroReservas.borrarReserva(numReserva);
	}


	// PRE (no es necesario comprobar): todas las solicitudes atendidas son válidas.
	public IList<Integer> getReservasRegistradasDesdeListaEspera(int i, int j) {
		IList<SolicitudReservaAnticipada> listaReservasAtendidas = 
				gestorLocalidad.getSolicitudesAtendidasListaEspera(i, j);
		IList<Integer> listaReservasRegistradas = new ArrayList<>();
		for (int k = 0 ; k < listaReservasAtendidas.size() ; k++) {
			listaReservasRegistradas.add(k, this.registroReservas.registrarReserva(listaReservasAtendidas.get(k)));
		}
		return listaReservasRegistradas;
	}
}
