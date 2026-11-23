package br.com.alura.screenmatch.principal;

import br.com.alura.screenmatch.excecao.ErrorDeConversaoDeAnoException;
import br.com.alura.screenmatch.modelos.Titulo;
import br.com.alura.screenmatch.modelos.TituloOmdb;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.github.cdimascio.dotenv.Dotenv;

import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class PrincipalComBusca {
    public static void main(String[] args) throws IOException, InterruptedException {
        Scanner leitura = new Scanner(System.in);
        System.out.println("Digite um filme para busca: ");
        var buscar = leitura.nextLine();

        String buscaCodificada = URLEncoder.encode(buscar, "UTF-8");

        Dotenv env = Dotenv.load();
        var apiKey = env.get("API_KEY");
        var apiUrl = env.get("API_URL");




        String endereco = apiUrl + buscaCodificada + "&apikey=" + apiKey;
        try {


            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(endereco))
                    .build();
            HttpResponse<String> response = client
                    .send(request, HttpResponse.BodyHandlers.ofString());

            String json = response.body();
            System.out.println(json);

            Gson gson = new GsonBuilder()
                    .setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
                    .create();
            //Titulo meuTitutlo = gson.fromJson(json, Titulo.class);
            TituloOmdb meuTitutloOmdb = gson.fromJson(json, TituloOmdb.class);
            System.out.println(meuTitutloOmdb);


        //try {
            Titulo meuTitutlo = new Titulo(meuTitutloOmdb);
            System.out.println("Titulo convertido");
            System.out.println(meuTitutlo);
            FileWriter escrita = new FileWriter("filmes.txt");
            escrita.write(meuTitutlo.toString());
            escrita.close();
        } catch (NumberFormatException e) {
            System.out.println("Aconteceu um erro: ");
            System.out.println(e.getMessage());

        }  catch (IllegalArgumentException e) {
            System.out.println("Aconteceu um erro de argumento: ");
        } catch (ErrorDeConversaoDeAnoException e) {
            System.out.println(e.getMensagem());
        }


        finally {
            System.out.println("O Programa finalizou corretamente.");
        }




    }
}
