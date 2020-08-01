package org.stemmer.state.of.art.Porter;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;


public class WriteFiles {
static String appendStr = "";
	public WriteFiles() throws IOException
	{
	}

	public void WriteGrava(String aux,int grava) throws IOException
	{

		if(grava == 100000)
		try(FileWriter fw = new FileWriter("./terminacoes/saida.txt", true);
			    BufferedWriter bw = new BufferedWriter(fw);
			    PrintWriter out = new PrintWriter(bw))
			{
			CopyOfPorterStemmer.gravastd = 0;
			if(!aux.equals(""))
			{
				appendStr += "\r\n"+aux;
			}
			out.println(appendStr);
			appendStr = "";

			} catch (IOException e) {
			    //exception handling left as an exercise for the reader
			}
		else
		{
			if(!aux.equals(""))
				appendStr += "\r\n"+aux;
		}
	}

		public void Write()
		{
			try(FileWriter fw = new FileWriter("./terminacoes/saida.txt", true);
				    BufferedWriter bw = new BufferedWriter(fw);
				    PrintWriter out = new PrintWriter(bw))
				    {
				    	out.println(appendStr);
				    }



				 catch (IOException e) {
				    //exception handling left as an exercise for the reader
				}
	}


}