package entite;

import javafx.event.EventHandler;
import javafx.scene.Group;
import javafx.scene.input.MouseEvent;

public class Plateau implements EventHandler<MouseEvent> {
	
	int[] base = {1,1,1,2,2,2,3,3,3,4,4,4};
	boolean[] possible = new boolean[9];
	Pion[] joueur1 = new Pion[12];
	Pion[] joueur2 = new Pion[12];
	Case[][] grille = new Case[8][7];
	int decalage;
	int tailleCase;
	int decalageTrait;
	Pion pionSelectionne;
	Group troupe = new Group();
	int point1 = 0;
	int point2 = 0;
	Score score = new Score();

	public Plateau(int decalage, int tailleCase, int decalageTrait) {
		this.decalage = decalage;
		this.tailleCase = tailleCase;
		this.decalageTrait = decalageTrait;
	}

	public Group dessinEnvironnement() {
		
		// dessin des lignes
		for (int i = 0; i < grille.length; i++) {
			for (int j = 0; j < grille[i].length; j++) {
				grille[i][j] = new Case(decalage + tailleCase * i, decalage + decalageTrait + tailleCase * j,
						tailleCase, tailleCase, i, j);
				troupe.getChildren().add(grille[i][j]);
				grille[i][j].setOnMouseClicked(this);

			}
		}
		
		initPion(troupe);
		
		Choix C1 = new Choix(0, tailleCase);
		Choix C2 = new Choix(1, tailleCase);
		troupe.getChildren().add(C1);
		troupe.getChildren().add(C2);
//		C1.setOnMouseMoved(new EventHandler<MouseEvent>(){
//			public void handle(MouseEvent me){
//				C1.setFill(Color.AQUA);
//			}
//		});
//		C1.setOnMouseExited(new EventHandler<MouseEvent>(){
//			public void handle(MouseEvent me){
//				C1.setFill(Color.GREY);
//			}
//		});
		C1.setOnMouseClicked(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent me){
				if(!C1.isSelected()){
					C2.switchColor();
					C1.switchColor();
				}
			}
		});
//		C2.setOnMouseMoved(new EventHandler<MouseEvent>(){
//			public void handle(MouseEvent me){
//				C2.setFill(Color.AQUA);
//			}
//		});
//		C2.setOnMouseExited(new EventHandler<MouseEvent>(){
//			public void handle(MouseEvent me){
//				C2.setFill(Color.GREY);
//			}
//		});
		C2.setOnMouseClicked(new EventHandler<MouseEvent>(){
			public void handle(MouseEvent me){
				if(!C2.isSelected()){
					C2.switchColor();
					C1.switchColor();
				}
			}
		});
		/*
		Pion pion = null; 
		int rand = 1;
		for (int i = 0; i < 8; i++) {
			for(int j = 0; j< 2; j++){
			rand =4 - i % 4;
			int rand2 = (int) (Math.random() * 2);
			pion = new Pion(decalage + tailleCase * i + tailleCase / 2,
					decalage + decalageTrait + tailleCase * j*6 + tailleCase / 2, tailleCase/2, rand, rand2, i, j*6);
			ajoutPion(pion, troupe);
			}
		}*/
		troupe.getChildren().add(score);
		return troupe;

	}
	
	public void initPion(Group troupe){
		Pion pion = null;
		Pion[] J = new Pion[12];
		int rand;
		for(int j = 0; j<2;j++){
			for(int l = 1; l<9;l++)
				possible[l] = false;
			for(int k = 0; k < 4; k++){
				for(int i = 0; i< 3;i++){
					rand = (int) (Math.random() * 7)+1;
					if(k == 1 || k == 3){
						pion = new Pion(decalage + tailleCase * J[3*k+i-3].getLigne() + tailleCase / 2,
								decalage + decalageTrait + tailleCase * j*6 + tailleCase / 2, tailleCase/2, base[3*k + i], j, J[3*k+i-3].getLigne(), j*6);
						J[3*k+i-3].setFils(pion);
						pion.setVisible(false);
					}else{
						while(possible[rand]){
							rand = (int) (Math.random() * 7)+1;
							
						}
						pion = new Pion(decalage + tailleCase * rand + tailleCase / 2,
						decalage + decalageTrait + tailleCase * j*6 + tailleCase / 2, tailleCase/2, base[3*k + i], j, rand, j*6);
						grille[rand][j*6].placePion(pion);
						possible[rand]=true;
					}
					J[3*k+i] = pion;
					pion.setOnMouseClicked(this);
					troupe.getChildren().add(pion);
				}
			}
			
		}
	}

	public void ajoutPion(Pion pion, Group troupe) {
		pion.setOnMouseClicked(this);
		grille[pion.getLigne()][pion.getCol()].placePion(pion);
		troupe.getChildren().add(pion);
	}

	public void majPoint(Pion p) {
		if(p.joueur == 0) {
			if(p.getCol() == grille[0].length-1) {
				point1 += p.getPoint();
			}
		}
		else if(p.joueur == 1) {
			if(p.getCol() == 0) {
				point2 += p.getPoint();
			}
		}
		score.setScore(point1, point2);
	}
	
	public void handle(MouseEvent me) {

		Object o = me.getSource();
		if (o instanceof Pion) {
			Pion d = (Pion) o;

//			System.out.println("clic en " + d.getLigne() + "," + d.getCol());
			// si on selectionne un nouveau pion ou deselectionne le pion deja selectionne
			if (pionSelectionne == null || pionSelectionne == d) {
				d.switchSelected();
				d.colorPion();
				if (pionSelectionne == null) {
					pionSelectionne = d;
					int x = pionSelectionne.getLigne();
					int y = pionSelectionne.getCol();
					System.out.println(x+"   "+y);
					for (int i = 0; i < grille.length; i++) {
						for (int j = 0; j < grille[i].length; j++) {
							if (grille[i][j].getLigne() == x + 1 && grille[i][j].getCol() == y + 1) {
								if (grille[i][j].isJouable(pionSelectionne))
									grille[i][j].ispossible();
								else
									grille[i][j].isimpossible();
							}
							if (grille[i][j].getLigne() == x && grille[i][j].getCol() == y + 1) {
								if (grille[i][j].isJouable(pionSelectionne))
									grille[i][j].ispossible();
								else
									grille[i][j].isimpossible();
							}
							if (grille[i][j].getLigne() == x - 1 && grille[i][j].getCol() == y + 1) {
								if (grille[i][j].isJouable(pionSelectionne))
									grille[i][j].ispossible();
								else
									grille[i][j].isimpossible();
							}
							if (grille[i][j].getLigne() == x + 1 && grille[i][j].getCol() == y) {
								if (grille[i][j].isJouable(pionSelectionne))
									grille[i][j].ispossible();
								else
									grille[i][j].isimpossible();
							}
							if (grille[i][j].getLigne() == x + 1 && grille[i][j].getCol() == y - 1) {
								if (grille[i][j].isJouable(pionSelectionne))
									grille[i][j].ispossible();
								else
									grille[i][j].isimpossible();

							}
							if (grille[i][j].getLigne() == x && grille[i][j].getCol() == y - 1) {
								if (grille[i][j].isJouable(pionSelectionne))
									grille[i][j].ispossible();
								else
									grille[i][j].isimpossible();
							}
							if (grille[i][j].getLigne() == x - 1 && grille[i][j].getCol() == y - 1) {
								if (grille[i][j].isJouable(pionSelectionne))
									grille[i][j].ispossible();
								else
									grille[i][j].isimpossible();
							}
							if (grille[i][j].getLigne() == x - 1 && grille[i][j].getCol() == y) {
								if (grille[i][j].isJouable(pionSelectionne))
									grille[i][j].ispossible();
								else
									grille[i][j].isimpossible();
							}
						}
					}
				} else {
					pionSelectionne = null;
					for (int i = 0; i < grille.length; i++) {
						for (int j = 0; j < grille[i].length; j++) {
							grille[i][j].resetColor();
						}
					}
				}
			}
		} else if (o instanceof Case && pionSelectionne != null) {
			Case r = (Case) o;
			boolean suppr = false;
			if (r.getEtat() == Etat.possible) {
				int x = pionSelectionne.getLigne();
				int y = pionSelectionne.getCol();
				if(r.Contenu()!=null){
				grille[x][y].placePion(pionSelectionne.getFils());
				pionSelectionne.setFils(r.Contenu());
				} else suppr = true;
				//System.out.println(r.Contenu());
				// le nouveau centre du jeton sera au centre du rectangle s�lectionn�
				double newX = r.getX() + tailleCase / 2;
				double newY = r.getY() + tailleCase / 2;
				pionSelectionne.switchSelected();
				
				pionSelectionne.deplacement(r.getLigne(), r.getCol(), newX, newY);
				
				pionSelectionne.setLigne(r.getLigne());
				pionSelectionne.setCol(r.getCol());
				for (int i = 0; i < grille.length; i++) {
					for (int j = 0; j < grille[i].length; j++) {
						if (grille[i][j].getLigne() == x && grille[i][j].getCol() == y && suppr) {
							grille[i][j].supprPion();
						}
						if (grille[i][j].getLigne() == pionSelectionne.getLigne()
								&& grille[i][j].getCol() == pionSelectionne.getCol()) {
							grille[i][j].placePion(pionSelectionne);
						}
						grille[i][j].resetColor();
					}
				}
				
				majPoint(pionSelectionne);

				pionSelectionne = null;
			}
		}
		
		else if (o instanceof Case ) {
		System.out.println(((Case) o).Contenu());
		}
		
	}

}