package br.com.alura.screenmatch.principal;

import java.util.Scanner;

import br.com.alura.screenmatch.excecao.ErroDeConversaoDeAnoException;
import br.com.alura.screenmatch.modelos.Titulo;
import br.com.alura.screenmatch.modelos.TituloOmdb;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.FileInputStream;
import java.util.Properties;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;


public class PrincipalComBusca {
    public static void main(String[] args) throws IOException, InterruptedException {
        String apiKey = null;
        try {
            Properties props = new Properties();
            FileInputStream fis = new FileInputStream("C:/Users/dougl/IdeaProjects/screenMatch/.env");
            props.load(fis);
            fis.close();
            apiKey = props.getProperty("API_KEY");
            System.out.println("API Key from file: " + apiKey);
        } catch (Exception e) {
            e.printStackTrace();
        }

        try{
            Scanner leitor = new Scanner(System.in);
            System.out.println("Digite o nome do filme que quer buscar:");
            String filme = leitor.nextLine();

            String url = "https://www.omdbapi.com/?t=" + filme.replace(" ", "+") + "&apikey=" + apiKey;

            System.out.println("API Key: " + apiKey);
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(url)).build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());


            String json = response.body();
            System.out.println("JSON response: " + json);

            Gson gson = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE).create();

            TituloOmdb meuTitulo = gson.fromJson(json, TituloOmdb.class);
            System.out.println(meuTitulo);
            Titulo meuTituloConvertido = new Titulo(meuTitulo);

            System.out.println("Título convertido: " + meuTituloConvertido);

        } catch (ErroDeConversaoDeAnoException e) {
            System.out.println("Aconteceu um erro: " + e.getMessage());
        } catch (IllegalArgumentException e) {
            System.out.println("O titulo de busca não é válido: " + e.getMessage());
        }
        System.out.println("Fim do programa");

    }
}
