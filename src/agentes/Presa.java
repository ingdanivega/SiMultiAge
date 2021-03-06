package agentes;

import java.awt.Color;
import java.awt.Graphics;
import java.util.Random;

import jade.core.Agent;
import jade.core.Location;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

import javax.swing.*;

@SuppressWarnings({ "serial", "unused" })
public class Presa extends Agent
{
	private String livre;
	Random obr = new Random();
	int dc[] = {0x00042000, 0x00042000, 0x0005A000, 0x0007E000, 0x000C3000, 0x001C3800, 0x001FF800, 0x001FF800, 
		    0x001FF800, 0x001FF800, 0x001FF800, 0x001FF800, 0x007FFE00, 0x00FFFF00, 0x01FFFF80, 0x03FFFFC0, 
		    0x03FFFFC0, 0x001FF800, 0x001FF800, 0x001FF800, 0x001FF800, 0x001FF800, 0x001FF800, 0x003FFC00, 
		    0x07FFFFE0, 0x07FFFFE0, 0x07FFFFE0, 0x07FFFFE0, 0x0007E000, 0x0003C000, 0x00018000},
			
		    /* Dibujo en sentido 2 (Abajo)*/
		    dcAb[] = {0x00018000, 0x0003C000, 0x0007E000, 0x07FFFFE0, 0x07FFFFE0, 0x07FFFFE0, 0x07FFFFE0, 0x003FFC00,
		    0x001FF800, 0x001FF800, 0x001FF800, 0x001FF800, 0x001FF800, 0x001FF800, 0x03FFFFC0, 0x03FFFFC0,
		    0x01FFFF80, 0x00FFFF00, 0x007FFE00, 0x001FF800, 0x001FF800, 0x001FF800, 0x001FF800, 0x001FF800,
		    0x001FF800, 0x001C3800, 0x000C3000, 0x0007E000, 0x0005A000, 0x00042000, 0x00042000},		
		    
		    /* Dibujo en sentido 3 (Izquierdo)*/
		    dcIz[] = {0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000078, 0x00018078, 0x00038078,
		    0x00078078, 0x000F80F8, 0x000F81F8, 0x07FFFFF8, 0x0FFFFFF8, 0xfFFFFFFC, 0x13FFFFFE, 0x33FFFFFF,
            0x33FFFFFF, 0x13FFFFFE, 0xFFFFFFFC, 0x0FFFFFF8, 0x07FFFFF8, 0x000F81F8, 0x000F80F8, 0x00078078,
            0x00038078, 0x00018078, 0x00000078, 0x00000000,	0x00000000, 0x00000000, 0x00000000, 0x00000000},
		    
            /* Dibujo en sentido 4 (Arriba)*/
            dcDe[] = {0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x1E000000, 0x1E00C000, 0x1E00E000,
		    0x1E00F000, 0x1F00F800, 0x1F80F800, 0x1FFFFFE0, 0x1FFFFFF0, 0x3FFFFFFF, 0x7FFFFFC8, 0xFFFFFFCC,
		    0xFFFFFFCC, 0x7FFFFFC8, 0x3FFFFFFF, 0x1FFFFFF0, 0x1FFFFFE0, 0x1F80F800, 0x1F00F800, 0x1E00F000,
		    0x1E00E000, 0x1E00C000, 0x1E000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000, 0x00000000};
	
	int x, y, rows, cols, sent_presa;
	
	public Presa()
	{
		rows = 18;cols = 18;
		y = rows*30;x = cols*30; // rows,cols
		sent_presa = 4;
	}
	public void setup() 
	{
		addBehaviour(new CompPrint("| Agente APPLE | - "+this.getAID().getName()));
		Object[] args = getArguments();
		if(args.length==1)
		{	
			livre = (String)args[0];
			addBehaviour(new CompPrint("Descripción -> "+livre));
		}
		else
		{
			addBehaviour(new CompPrint("No hay descripción."));
			doDelete();
		}
		addBehaviour(new CyclicBehaviour() {
			
			public void action()
			{
				//MessageTemplate mt = MessageTemplate.and(MessageTemplate.MatchPerformative(ACLMessage.INFORM), MessageTemplate.MatchOntology("move"));
				ACLMessage message = receive();
				if(message!=null)
				{
					System.out.println("Mens. Apple: >>>> "+message.getContent());
					String cad = message.getContent();
					String[] tot = cad.split("-");
					int ro = Integer.parseInt(tot[0]);
					int co = Integer.parseInt(tot[1]);
					System.out.println(" ------------------------------ "+ro);
				}
				else
					block();
			}
		});
	}
	
	/* Método que decide a dónde moverse en base a los valores del ambiente. */
	public void Mover(double left, double straight, double right)
	{
		System.out.println("["+rows+","+cols+"]");
		System.out.println("PRESA - Izquierda: "+left);
		System.out.println("PRESA - Enfrente: "+straight);
		System.out.println("PRESA - Derecha: "+right);
		System.out.println("\n");
		
		try
		{
			Thread.sleep(300);
		}
		catch(InterruptedException e){}
		
		double des=0;int selected=0;
		double arr[] = {left,straight,right};
		// Movementos y condiciones
		if(cols>=0 && cols<=19 && rows>=0 && rows<=19) // Dentro del margen
		{
			for (int i = 0; i < arr.length; i++)
			{
				if(arr[i] > des)
				{
					des = arr[i];
					selected = i;
				}
			}
			if(des==1.0)
				System.out.println("Om nom nom nom...\n");
			if(left==straight && left==right && straight==right)
				selected = 1;	
			
			// Dirección 1 -> Derecha
			if(sent_presa == 1 && selected == 0) // izquierda
			{
				x+=30;
				y-=30;
				rows--;
				cols++;
			}
			if(sent_presa == 1 && selected == 1) // enfrente
			{
				if(cols==19)
				{
					System.out.println('x');
				}
				else
				{
					x+=30;
					cols++;
				}
				
			}
			if(sent_presa == 1 && selected == 2) // derecha
			{
				x+=30;
				y+=30;
				rows++;
				cols++;
			}
			
			// Dirección 2 -> Abajo
			if(sent_presa == 2 && selected == 0) // izquierda
			{
				x+=30;
				y+=30;
				rows++;
				cols++;
			}
			if(sent_presa == 2 && selected == 1) // enfrente
			{
				if(rows==19)
				{
					System.out.println('x');
				}
				else
				{
					y+=30;
					rows++;
				}
			}
			if(sent_presa == 2 && selected == 2) // derecha
			{
				x-=30;
				y+=30;
				rows++;
				cols--;
			}
			
			// Dirección 3 -> Izquierda
			if(sent_presa == 3 && selected == 0) // izquierda
			{
				x-=30;
				y+=30;
				rows++;
				cols--;
			}
			if(sent_presa == 3 && selected == 1) // enfrente
			{
				if(cols==0)
				{
					System.out.println('x');
				}
				else
				{
					x-=30;
					cols--;
				}
			}
			if(sent_presa == 3 && selected == 2) // derecha
			{
				x-=30;
				y-=30;
				rows--;
				cols--;
			}
			
			// Dirección 4 -> Arriba
			if(sent_presa == 4 && selected == 0) // izquierda
			{
				x-=30;
				y-=30;
				rows--;
				cols--;
			}
			if(sent_presa == 4 && selected == 1) // enfrente
			{
				if(rows==0)
				{
					System.out.println('x');
				}
				else
				{
					y-=30;
					rows--;
				}
			}
			if(sent_presa == 4 && selected == 2) // derecha
			{
				x+=30;
				y-=30;
				rows--;
				cols++;
			}
			
			// VERIFICACIONES: -
			
			if(x==570)
			{
				int dirs[] = {2,3,4} ;
				sent_presa = dirs[obr.nextInt(2)];
				System.out.println("Llegué al borde derecho.");
			}
			if(x==0)
			{
				int dirs[] = {1,2,4} ;
				sent_presa = dirs[obr.nextInt(2)];
				System.out.println("Llegué al borde izquierdo.");
			}
			if(y==540)
			{
				int dirs[] = {1,3,4} ;
				sent_presa = dirs[obr.nextInt(2)];
				System.out.println("Llegué al borde inferior..");
			}
			if(y==0)
			{
				int dirs[] = {1,2,3} ;
				sent_presa = dirs[obr.nextInt(2)];
				System.out.println("Llegué al borde superior.");
			}
		}
	}
	
	public void Pintar(Graphics g) 
	{
		/* Verifica el sentido el cuál cambiará el dibujo de posición. */
		if(sent_presa == 1)
		{
			g.setColor(Color.pink);
			for (int i = 0; i < dcDe.length; i++) 
				for (int j = 0; j < 32; j++)
				{
					int desp = 0x8000000>>j;
					int res = dcDe[i]&desp;
					if(res != 0)
						g.drawLine(x+j, y+i, x+j, y+i);
				}
		}
		if(sent_presa == 2)
		{
			g.setColor(Color.pink);
			for (int i = 0; i < dcAb.length; i++) 
				for (int j = 0; j < 32; j++)
				{
					int desp = 0x8000000>>j;
					int res = dcAb[i]&desp;
					if(res != 0)
						g.drawLine(x+j, y+i, x+j, y+i);
				}
		}
		if(sent_presa == 3)
		{
			g.setColor(Color.pink);
			for (int i = 0; i < dcIz.length; i++) 
				for (int j = 0; j < 32; j++)
				{
					int desp = 0x8000000>>j;
					int res = dcIz[i]&desp;
					if(res != 0)
						g.drawLine(x+j, y+i, x+j, y+i);
				}
		}
		if(sent_presa == 4)
		{
			g.setColor(Color.pink);
			for (int i = 0; i < dc.length; i++) 
				for (int j = 0; j < 32; j++)
				{
					int desp = 0x8000000>>j;
					int res = dc[i]&desp;
					if(res != 0)
						g.drawLine(x+j, y+i, x+j, y+i);
				}
		}
	}
	
	public void takeDown()
	{
		addBehaviour(new CompPrint("Maraton a PRESA... :("));
	}
	@Override
	public void doMove(Location loc)
	{
		addBehaviour(new CompPrint("Migración vers: "+loc.getName()));
	}
}
