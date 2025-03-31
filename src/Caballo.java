package progii.juegotablero.model.ajedrez.piezas;

import list.IList;
import list.ArrayList;
import progii.juegotablero.model.Casilla;
import progii.juegotablero.model.Jugador;
import progii.juegotablero.model.ajedrez.PiezaAjedrez;
import progii.juegotablero.model.ajedrez.TipoPiezaAjedrez;

public class Caballo extends PiezaAjedrez{

	public Caballo(Jugador jugador, int fila, char columna) {
		super(jugador, TipoPiezaAjedrez.CABALLO, fila, columna);
	}

	@Override
	public IList<Casilla> movimientosValidos(){
		/*IList<Casilla> resultado = new ArrayList<>();
		
		int [][] movimientosPosibles = {{-2, 1}, {-2, -1}, {2, 1}, {2, -1}, {1, -2}, {1, 2}, {-1, -2}, {-1, 2}};
		
		for(int i = 0; i< movimientosPosibles.length; i++) {
			int movimientoActualFila = this.getFila() + movimientosPosibles [i][0];
			char movimientoActualColumna = (char) (this.getColumna() + movimientosPosibles [i][1]);
			
			if(movimientoDentroLimites (movimientoActualFila, movimientoActualColumna)) {
			PiezaAjedrez pieza = queHay (movimientoActualFila, movimientoActualColumna);
			if(pieza == null || pieza.getJugador().getId() != this.getJugador().getId()) {
				resultado.add(resultado.size(), new Casilla (movimientoActualFila, movimientoActualFila));
			}
			}
			
		}
		return resultado;*/
		
		
		
		IList<Casilla> resultado = new ArrayList<>();
		//Horizontal superior izquierda según el modelo interno (matriz)
				casillaVisitable (resultado, 1, -2);
				//Horizontal inferior izquierda según el modelo interno (matriz)
				casillaVisitable (resultado, -1, -2);
						
				//Horizontal derecha según el modelo interno (matriz)
				casillaVisitable (resultado, 1, 2);
				//Horizontal derecha según el modelo interno (matriz)
				casillaVisitable (resultado, -1, 2);
						
				//Vertical superior izquierda según el modelo interno (matriz)
				casillaVisitable (resultado, 2, -1);
				//Vertical inferior izquierda según el modelo interno (matriz)
				casillaVisitable (resultado, -2, -1);
				
				//Vertical superior derecha según el modelo interno (matriz)
				casillaVisitable (resultado, 2, 1);
				//Vertical inferior derecha según el modelo interno (matriz)
				casillaVisitable (resultado, -2, 1);
				
				/*for(int i = 0; i < resultado.size(); i++) {
					if(distManhattan(resultado.get(i).getX(), resultado.get(i).getY()) != 3) {
						resultado.removeElementAt(i);
					}
				}*/
				
				/*int i=0;
				while (i<resultado.size()) {
					Casilla nuevaCasilla = resultado.get(i);
					if(distManhattan (nuevaCasilla.getX(), nuevaCasilla.getY()) != 3) {
						resultado.removeElementAt(i);
					}
					else
						i++;
				}*/
				return resultado;
			}


}
