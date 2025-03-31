package modelo.reservas.solicitudesreservas;

import java.time.LocalDateTime;
import modelo.gestoresplazas.GestorLocalidad;
import modelo.gestoresplazas.GestorZona;
import modelo.vehiculos.Vehiculo;

public class SolicitudReservaAnticipada extends SolicitudReserva {
	
	public SolicitudReservaAnticipada(int i, int j, LocalDateTime tI, LocalDateTime tF, Vehiculo vehiculo) {
		super(i, j, tI, tF, vehiculo);
	}
	
	@Override
	public void gestionarSolicitudReserva(GestorLocalidad gestor) {
		super.gestionarSolicitudReserva(gestor);
		GestorZona gestorZona = gestor.getGestorZona(this.getIZona(), this.getJZona());
		if (this.getHueco() == null) {
			gestorZona.meterEnListaEspera(new SolicitudReservaAnticipada(this.getIZona(), this.getJZona(), 
										this.getTInicial(), this.getTFinal(), this.getVehiculo()));
		}
	}
}
