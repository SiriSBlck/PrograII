package modelo.gestoresplazas;

import java.time.LocalDateTime;
import java.util.Arrays;

import list.ArrayList;
import list.IList;
import modelo.gestoresplazas.huecos.GestorHuecos;
import modelo.gestoresplazas.huecos.Hueco;
import modelo.gestoresplazas.huecos.Plaza;
import modelo.reservas.solicitudesreservas.SolicitudReservaAnticipada;

public class GestorZona {
	private int iZona;
	private int jZona;
	private Plaza[] plazas;
	private double precio;
	private IList<SolicitudReservaAnticipada> listaEspera;
	private GestorHuecos gestorHuecos;
	private IList<Hueco> huecosReservados; 
	//implementado por ArrayList y LinkedList, más restrictivo pero más versátil ante cambios/adiciones futuras

	public int getI() {
		return iZona;
	}

	public int getJ() {
		return jZona;
	}

	public double getPrecio() {
		return precio;
	}

	public String getId() {
		return "z" + iZona + ":" + jZona;
	}

	public String getEstadoHuecosLibres() {
		return this.gestorHuecos.toString();
	}

	public String getEstadoHuecosReservados() {
		return this.huecosReservados.toString();
	}

	public String getListaEspera() {
		return this.listaEspera.toString();
	}

	public String getPlazas() {
		return Arrays.toString(this.plazas);
	}

	public String toString() {
		return getId() + ": " + getEstadoHuecosReservados();
	}

	//TO-DO alumno obligatorios

	public GestorZona(int i, int j, int noPlazas, double precio) {
		this.iZona = i;
		this.jZona = j;
		this.precio = precio;
		this.plazas = new Plaza[noPlazas]; //inicializa el array de plazas
		for(int k = 0 ; k < noPlazas ; k++) { //inicializa cada plaza
			plazas[k] = new Plaza (k);
		}
		this.listaEspera = new ArrayList<>(); //implementa la interfaz IList, por lo que estos arrayList 
		this.huecosReservados = new ArrayList<>(); //solo puede usar los métodos de IList
		this.gestorHuecos = new GestorHuecos(this.plazas);
	}

	public boolean existeHuecoReservado(Hueco hueco) {
		return this.huecosReservados.indexOf(hueco) != -1;
	}

	public boolean existeHueco(LocalDateTime tI, LocalDateTime tF) {
		return gestorHuecos.existeHueco(tI, tF);
	}

	public Hueco reservarHueco(LocalDateTime tI, LocalDateTime tF) {
		Hueco hueco = this.gestorHuecos.reservarHueco(tI, tF); //por defecto si no existe devuelve null
		if(hueco != null) {
			huecosReservados.add(huecosReservados.size(), hueco);
		}
		return hueco; 
	}

	
	public void meterEnListaEspera(SolicitudReservaAnticipada solicitud) {
		this.listaEspera.add(listaEspera.size(), solicitud);
	}

	
	//PRE (no es necesario comprobar): las solicitudes de la lista de espera son válidas
	public IList<SolicitudReservaAnticipada> getSolicitudesAtendidasListaEspera() {		
		IList<SolicitudReservaAnticipada> solicitudesAtendidasListaEspera = new ArrayList<>();
		for(int i = 0; i < this.listaEspera.size() && 
				solicitudesAtendidasListaEspera.size() <= plazas.length; i++) {
			if(existeHueco(this.listaEspera.get(i).getTInicial(), this.listaEspera.get(i).getTFinal())) {
				SolicitudReservaAnticipada solicitudAtendida = this.listaEspera.get(i);
				solicitudAtendida.setHueco(reservarHueco(solicitudAtendida.getTInicial(), solicitudAtendida.getTFinal()));
				solicitudesAtendidasListaEspera.add(solicitudesAtendidasListaEspera.size(), solicitudAtendida);
				this.listaEspera.removeElementAt(i);
			}
		}
		
		return solicitudesAtendidasListaEspera;		//Solo algunas reservas se pueden atender
	} 
	

	//TO-DO alumno opcionales
	public void liberarHueco(Hueco hueco) {
		this.huecosReservados.remove(hueco);
		this.gestorHuecos.liberarHueco(hueco);
	}

}
