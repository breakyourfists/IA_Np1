package algoritmo;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;

public class Ladrao extends ProgramaLadrao {

	private int direcao;
	private Random rd;

	private final int CIMA=7, BAIXO=16,ESQUERDA=11, DIREITA=12;
	private final int[] direcoes = new int[] { CIMA, BAIXO, ESQUERDA, DIREITA };

	ArrayList<Point> historico = new ArrayList<>();

	public Ladrao() {
		super();
		rd = new Random();

		direcao = direcoes[rd.nextInt(3)];
	}


	public int acao() {
		int posPoupador = getPoupadorPorVisao();

		if (posPoupador != -1) {
			direcao = seguirPorVisao(posPoupador);

		} else {
			if ((posPoupador = getPoupadorPorCheiro()) != -1) {
				direcao = seguirPorCheiro(posPoupador);
			} else {
				direcao = alteraSentido();
			}
		}

		historico.add(sensor.getPosicao());

		if(isPreso()){
			direcao = alteraSentido();
		}

		return caminhar(direcao);
	}



	private int getPoupadorPorVisao() {
		int[] visao = sensor.getVisaoIdentificacao();
		for (int i = 0; i < visao.length; i++) {
			if (visao[i] == 100 || visao[i]== 110) {
				return i;
			}
		}
		return -1;
	}

	private boolean isObstaculo(int pos) {
		int[] visao = sensor.getVisaoIdentificacao();
		if (visao[pos] != 0) {
			return true;
		} else {
			return false;
		}
	}

	private int caminhar(int andar) {
		if (andar == CIMA) {
			return 1;
		} else if (andar == BAIXO) {
			return 2;
		} else if (andar == DIREITA) {
			return 3;
		} else if (andar == ESQUERDA) {
			return 4;
		} else {
			return 0;
		}
	}

	private int getPoupadorPorCheiro() {
		int[] cheiro = sensor.getAmbienteOlfatoPoupador();
		int menorCheiro = 6;
		int retorno = -1;
		for (int i = 0; i < cheiro.length; i++) {
			if (cheiro[i] < menorCheiro && cheiro[i] != 0 && cheiro[i] != -1) {
				menorCheiro = cheiro[i];
				retorno = i;
			}
		}
		return retorno;
	}

	private int seguirPorVisao(int posPoupador) {
		if (posPoupador == 5 || posPoupador == 10 || posPoupador == 14 || posPoupador == ESQUERDA) {
			return ESQUERDA;
		} else if (posPoupador == 9 || posPoupador == 13 || posPoupador == 18 || posPoupador == DIREITA) {
			return DIREITA;
		} else if (posPoupador == 1 || posPoupador == 2 || posPoupador == 3 || posPoupador == CIMA) {
			return CIMA;
		} else if (posPoupador == 20 || posPoupador == 21 || posPoupador == 22 || posPoupador == BAIXO) {
			return BAIXO;
		} else if ((posPoupador == 0 || posPoupador == 6) && !isObstaculo(CIMA)) {
			return CIMA;
		} else if ((posPoupador == 0 || posPoupador == 6) && !isObstaculo(ESQUERDA)) {
			return ESQUERDA;
		} else if ((posPoupador == 4 || posPoupador == 8) && !isObstaculo(CIMA)) {
			return CIMA;
		} else if ((posPoupador == 4 || posPoupador == 8) && !isObstaculo(DIREITA)) {
			return DIREITA;
		} else if ((posPoupador == 15 || posPoupador == 19) && !isObstaculo(ESQUERDA)) {
			return ESQUERDA;
		} else if ((posPoupador == 15 || posPoupador == 19) && !isObstaculo(BAIXO)) {
			return BAIXO;
		} else if ((posPoupador == CIMA || posPoupador == 23) && !isObstaculo(DIREITA)) {
			return DIREITA;
		} else if ((posPoupador == CIMA || posPoupador == 23) && !isObstaculo(BAIXO)) {
			return BAIXO;
		} else {
			return direcao;
		}
	}

	private int seguirPorCheiro(int poupador) {

		if (poupador == 1) {
			return CIMA;
		} else if (poupador == 3) {
			return ESQUERDA;
		} else if (poupador == 4) {
			return DIREITA;
		} else if (poupador == 6) {
			return BAIXO;
		} else if ((poupador == 0) && !isObstaculo(CIMA)) {
			return CIMA;
		} else if ((poupador == 0) && !isObstaculo(ESQUERDA)) {
			return ESQUERDA;
		} else if ((poupador == 2) && !isObstaculo(CIMA)) {
			return CIMA;
		} else if ((poupador == 2) && !isObstaculo(DIREITA)) {
			return DIREITA;
		} else if ((poupador == 5) && !isObstaculo(BAIXO)) {
			return BAIXO;
		} else if ((poupador == 5) && !isObstaculo(ESQUERDA)) {
			return ESQUERDA;
		} else if ((poupador == CIMA) && !isObstaculo(BAIXO)) {
			return BAIXO;
		} else if ((poupador == CIMA) && !isObstaculo(DIREITA)) {
			return DIREITA;
		} else {
			return direcao;
		}
	}

	private int alteraSentido() {
		int novaDirecao = direcao;
		while (novaDirecao == direcao) {
			if (isVertical(direcao)) {
				return new int[] { ESQUERDA, DIREITA }[rd.nextInt(2)];
			} else {
				return new int[] { CIMA, BAIXO }[rd.nextInt(2)];
			}
		}

		return direcao;
	}

	private boolean isVertical(int pos) {
		if (pos == CIMA || pos == BAIXO)
			return true;
		return false;
	}

	private boolean isPreso() {
		int contador=0;
		if(historico.size()>200)
			historico = new ArrayList<>();

		int tamanho = historico.size();

		if(tamanho<5)
			return false;

		for (int i = 1; i < 6; i++) {
			if(sensor.getPosicao().equals(historico.get(tamanho-i))){
				contador++;
			}else
				contador--;
		}
		return contador>=5;

	}

}
