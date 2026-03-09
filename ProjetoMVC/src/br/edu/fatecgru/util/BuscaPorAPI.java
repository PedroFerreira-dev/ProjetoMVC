package br.edu.fatecgru.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;

import br.edu.fatecgru.model.Endereco;

//Tudo que envolve API
public class BuscaPorAPI {
	
	public String buscarSiglaPorEstado(String estadoSelecionado) {
		
		try {
            //Pega a sigla com base no Estado selecionado
            URL urlEstados = new URL("https://servicodados.ibge.gov.br/api/v1/localidades/estados");
            HttpURLConnection conexao = (HttpURLConnection) urlEstados.openConnection();
            conexao.setRequestMethod("GET");
            conexao.connect();

            BufferedReader leitor = new BufferedReader(new InputStreamReader(conexao.getInputStream()));
            StringBuilder resposta = new StringBuilder();
            String linha;

            while ((linha = leitor.readLine()) != null) {
                resposta.append(linha);
            }
            leitor.close();

            JSONArray estados = new JSONArray(resposta.toString());
            String sigla = "";
            for (int i = 0; i < estados.length(); i++) {
                JSONObject estado = estados.getJSONObject(i);
                String nome = estado.getString("nome");
                if (nome.equalsIgnoreCase(estadoSelecionado))
                {
                    sigla = estado.getString("sigla");
                    return sigla;
                }
            }
            
            //Em caso de erro, retorna null
            return null;
		} 
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	
	
	public List<String> buscarCidadesPorSigla(String siglaEstado) {
		
		try {
			//Pega as cidades com base na sigla do Estado
			URL urlCidades = new URL("https://servicodados.ibge.gov.br/api/v1/localidades/estados/" + siglaEstado + "/municipios");
			HttpURLConnection conexao = (HttpURLConnection) urlCidades.openConnection();
			conexao.setRequestMethod("GET");
			conexao.connect();

			BufferedReader leitor = new BufferedReader(new InputStreamReader(conexao.getInputStream()));
			StringBuilder resposta = new StringBuilder();
			String linha;

			while ((linha = leitor.readLine()) != null) {
				resposta.append(linha);
			}
			leitor.close();

			JSONArray cidadesArray = new JSONArray(resposta.toString());
			
			List<String> cidades = new java.util.ArrayList<>();
			for (int i = 0; i < cidadesArray.length(); i++) {
				JSONObject cidade = cidadesArray.getJSONObject(i);
				String nomeCidade = cidade.getString("nome");
				cidades.add(nomeCidade);
			}
			
			// Ordenar alfabeticamente
	        Collections.sort(cidades, String.CASE_INSENSITIVE_ORDER);
			
			return cidades;
		} 
		catch (Exception e) {
			e.printStackTrace();
			return null;
		}	
	}
	
	//Busca o endereço completo com base no CEP
	public Endereco buscarEnderecoPorCEP(String cep) {
		
	    try {
	        // Pega o endereço com base no CEP
	        URL urlCEP = new URL("https://viacep.com.br/ws/" + cep + "/json/");
	        HttpURLConnection conexao = (HttpURLConnection) urlCEP.openConnection();
	        conexao.setRequestMethod("GET");
	        conexao.connect();
	        
	        

	        BufferedReader leitor = new BufferedReader(new InputStreamReader(conexao.getInputStream()));
	        StringBuilder resposta = new StringBuilder();
	        String linha;

	        while ((linha = leitor.readLine()) != null) {
	            resposta.append(linha);
	        }
	        leitor.close();

	        JSONObject enderecoJson = new JSONObject(resposta.toString());
	        
	        //Verifica se o CEP existe
	        if (enderecoJson.has("erro") && enderecoJson.getBoolean("erro")) {
	        	
	        	//Se não existir, retorna null
	            return null;
	        }

	        Endereco endereco = new Endereco();
	        endereco.setUf(enderecoJson.getString("uf"));
	        endereco.setCidade(enderecoJson.getString("localidade"));
	        endereco.setLogradouro(enderecoJson.getString("logradouro"));
	        endereco.setBairro(enderecoJson.getString("bairro"));
	        endereco.setEstado(enderecoJson.getString("estado"));

	        return endereco;
	    } catch (Exception e) {
	        e.printStackTrace();
	        return null;
	    }
	}
	
	//Normaliza texto para comparação com a API, evitando erros com acentuação e maiúsculas/minúsculas
	public String normalizar(String texto) {
	    if (texto == null) return "";
	    return java.text.Normalizer.normalize(texto, java.text.Normalizer.Form.NFD)
	            .replaceAll("[^\\p{ASCII}]", "")
	            .toLowerCase()
	            .trim();
	}
}
