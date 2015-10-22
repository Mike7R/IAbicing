
import IA.Bicing.Estacion;


public class Furgo {

	private Integer nbicicletes;
	/**
	 * Capacitat max de les furgos.
	 */
	public static final Integer MAX = 30;
	/**
	 * Numero maximo de viajes que puede hacer.
	 * Notese que la recogida inicial tambien cuenta.
	 */
	public static final Integer MAX_VIAJES = 3;

	public iPair[] dest;
	public Integer i;

	Furgo() {
		dest = new iPair[MAX_VIAJES];
		nbicicletes = i = 0;
	}

	void enviar(Integer estacio, Integer q)
	{
		dest[i] = new iPair(estacio, q);
		i++;
		nbicicletes += q;
	}

	/**
	 * Modifica una recogida. PRE: v < f.i; si v == 0 -> q>=0;
		 *
	 * @param e estacion
	 * @param q cantidad
	 * @param v viaje
	 */
	void modificar_recogida(int v, Integer q)
	{
		if (v == 0)
		{
			if (q == 0)
			{
				i = nbicicletes = 0;
			} else
			{
				i = 1;
				dest[0].i2 = nbicicletes = q;
			}
		} else
		{
			nbicicletes -= q - dest[v].i2;
			if (q == 0)
			{
				for (int v2 = v + 1; v2 < i; ++v2)
				{
					dest[v2 - 1] = dest[v2];
				}
				--i;
			} else
			{
				// FIXME Propagar cantidad
				dest[v].i2 = q // para que el compilador se queje
			}
		}


	}
	/**
	 * Retorna si la furgoneta puede recoger bicicletas.
	 * Una de las restricciones es que solo se puede en la primera estacion.
	 * @return CIERTO si puede recoger bicicletas
	 */
	public Boolean pot_recollir()
	{
		/* en principi i = 0 -> nbicicletes = 0
		pero deixarem la comprobacio ...*/
		return i == 0 && nbicicletes < Furgo.MAX;
	}

	/**
	 * Retorna si la furgoneta pot entregar bicicletes
	 *
	 * @return TRUE si pot entregar
	 */
	public Boolean pot_entregar()
	{
		return nbicicletes > 0 && i < Furgo.MAX_VIAJES;
	}

	public Furgo copia(){
		Furgo a = new Furgo();
		for ( a.i=0; a.i < i; ++a.i) {
			a.dest[a.i] = dest[a.i].copia();
			a.nbicicletes = nbicicletes;
		}
		return a;
	}

	public Integer getbicicletes()
	{
		return nbicicletes;
	}

	public Integer getbiciclietes(int v)
	{
		Integer b = 0;
		for (int i = 0; i < v && i < this.i; ++i)
		{
			b += dest[i].i2;
		}
		return b;
	}

	/**
	 * bicicletes que sobren
	 *
	 * @return 0 si no sobre cap bicicleta o si encara queden viatges, el numero
	 * de bicicletes que es van agafar de mes en altre cas.
	 */
	public Integer bicicletessobren()
	{
		return i < Furgo.MAX_VIAJES ? 0 : nbicicletes;
	}

	/**
	 * Se carga las bicicletas que sobran o no hace nada si no sobran.
	 */
	public void canonizar()
	{
		// si todo va bien i nunca sera > MAXVIAJES >.<
		if (i == Furgo.MAX_VIAJES)
		{
			dest[0].i2 -= nbicicletes;
		}
	}

	/**
	 * Retorna el coste del viaje.
	 *
	 * @return El coste total en combustible que custa el viaje
	 */
	public double coste_combustible()
	{
		if (i > 0)
		{
			double coste = 0;
			int nb = dest[0].i2;
			for (int x = 1; x < i; ++x)
			{
				coste += ((nb + 9) / 10) * distancia(Main.Problema.get(dest[x-1].i1),
						Main.Problema.get(dest[x].i1));
				nb += dest[x].i2;
			}
			return coste;
		} else
			return 0;
	}

	/**
	 *
	 * @return Distancia total recorrida (km)
	 */
	public double distancia_recorrida()
	{
		double d = 0;
		for (int x = 1; x < i; ++x)
		{
			d += distancia(Main.Problema.get(dest[x-1].i1),
					Main.Problema.get(dest[x].i1));
		}
		return d;
	}

	/**
	 * Distancia Manhattan entre dos estaciones
	 *
	 * @param e1 Estacion 1
	 * @param e2 Estacion 2
	 *
	 * @return La distancia Manhattan entre las dos estaciones. (km)
	 */
	private double distancia(Estacion e1, Estacion e2)
	{
		return (Math.abs(e1.getCoordX() - e2.getCoordX())
				+ Math.abs(e1.getCoordY() - e2.getCoordY())) / 1000;
	}
}
