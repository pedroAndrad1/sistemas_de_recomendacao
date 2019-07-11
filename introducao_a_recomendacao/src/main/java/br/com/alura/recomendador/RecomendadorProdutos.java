package br.com.alura.recomendador;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.eval.RecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.eval.AverageAbsoluteDifferenceRecommenderEvaluator;
import org.apache.mahout.cf.taste.impl.model.file.FileDataModel;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.RecommendedItem;
import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;
import org.apache.mahout.common.RandomUtils;

public class RecomendadorProdutos {

	public static void main(String[] args) throws IOException, TasteException {
		// TODO Auto-generated method stub
		/*
		 * 		Um recommender recomenda itens a user's baseado no conceito de similaridade.
		 * Ele procura um ou mais outros user's no csv que sejam similares ao user o qual
		 * ele fara a recomendacao. A partir das notas que estes um ou mais user's deram aos itens,
		 * ele recomenda itens os quais ele acha que o user em questao gostaria, ou seja, daria uma
		 * boa nota. Alem disso, o recommender tambem faz estimativas das notas que o user daria aos itens
		 * recomendado.
		 * 		Para criar o recommender que irá recomendar itens para user's representados
		 * por vetores, seguiremos os seguintes passos.
		 * 
		 * OBS: Existem outras classes para fazerem essas funcoes. Cada classe trabalha de uma forma diferente
		 * e cabe a nos escolher qual usar.
		 */
		
		/*
		 * 1 - Importar o csv usando a classe File, que recebe o caminho do csv como parametro
		 * em String, e criar um modelo baseado nesse usando a classe FileDataModel que recebe
		 * como parametro o File criado anteriormente.
		 */
		
		File file = new File("dados.csv");
		FileDataModel model = new FileDataModel (file);
		
		/*
		 * 2 - Será preciso uma funcao para comparar a similaridade entre os user's. Para isso
		 * sera usada classe PearsonCorrelationSimilarity que implementa a interface UserSimilarity.
		 * Esta classe recebe como parametro um model, nesse caso um FileDataModel criado no passo
		 * anterior. Existem outras classes para ser essa funcao.
		 */
		
		UserSimilarity similarity = new PearsonCorrelationSimilarity(model);
		
		/*
		 * 3 - Agora é preciso uma funcao que calcule a proximidade entre os user's, vulgo neighborhood.
		 * Esta sera a classe ThresholdUserNeighborhood, que implementa a interface UserNeighborhood.
		 * Esta classe recebe como parametro um valor para considerar user's proximos um do outro, este
		 * valor e um float que quanto mais proximo de zero, mais similares os user's precisam ser para
		 * serem considerados proximos, uma funcao de similaridade que nesse caso sera a 
		 * PearsonCorrelationSimilarity e um modelo que e o nosso FileDataModel.
		 */
		
		UserNeighborhood neighborhood = new ThresholdUserNeighborhood(0.1, similarity, model);
		
		/*
		 * Ja temos oq e necessario para criar o recommender. Este sera a classe GenericUserBasedRecommender,
		 * que implementa a interface UserBasedRecommender e recebe como parametro um Model, uma funcao de 
		 * proximidade que sera a ThresholdUSerNeighborhood e uma funcao de  similaridae que sera a 
		 * PearsonCorrelationSimilarity
		 */
		
		UserBasedRecommender recommender = new GenericUserBasedRecommender(model, neighborhood, similarity);
		
		/*
		 * Agora vamos usar a funcao recommend do recommender, esta recebe como parametro dois int's,
		 * o primeiro representa o user no csv e so segundo e a ate quantas recomendacoes serao feitas.
		 * Note que as vezes nao e possivel fazer todas as N recomendacoes. Esta funcao retorna uma lista
		 * de objetos da classe RecommededItem, estes contem o item recomendado e a nota estimada pelo
		 * recommender.
		 */
		
		List<RecommendedItem> recommendations = recommender.recommend(2,3);
		
		//Farei um for each para  imprimir as recomendations feitas pelo recommender.
		for(RecommendedItem recommendation: recommendations) {
			System.out.println(recommendation);
		}
		
		/*
		 * Mas como todo modelo, há erros. Para calcular a margem de erro do recommender, sera usada
		 * a classe AverageAbsoluteDifferenceRecommenderEvaluator que implementa a interface 
		 * RecommenderEvaluator. Esta classe recebe 4 parametros: A primeira informação é um construtor 
		 * de recomendações, que ainda não temos, mas iremos criar. Este construtor sera uma classe que 
		 * implementa a interface RecommenderBuilder, esta interface tem uma funcao chamada 
		 * buildRecommender() que devolver um recommender. O segundo é um construtor de modelos, 
		 * mas para este há uma alternativa no terceiro argumento no qual podemos passar o modelo pronto 
		 * e os últimos dois argumentos que são a quantidade da amostra em porcentagem que desejamos
		 *  utilizar para treinar o algoritmo e também testá-lo.
		 * 
		 * OBS: O RecommenderEvaluator seleciona aleatoriamente dados para treino e para teste.
		 * E preciso definir uma seed para a escolha dos dados.
		 */
		
		//Esta funcao fixa uma seed para a escolha dos dados para treino e teste.
		RandomUtils.useTestSeed();
		
		RecommenderEvaluator evaluator = new  AverageAbsoluteDifferenceRecommenderEvaluator();
		RecommenderBuilder builder = new RecomendadorBuilder();
		
		//Estou definindo que 90% dos dados sao para treino e 100% sao para teste.
		double erro = evaluator.evaluate(builder, null, model, 0.9, 1.0);
		
		System.out.println(erro);
		
		
		/*
		 * Esse dados.csv e um csv que o mahout nos da para testar. Vamos usar dados reais agora.
		 */
		
		//Importando o arquivo, poderia colocar esse passo em uma funcao ou algo assim, mas preguica.
		
		File file1 = new File("cursos.csv");
		FileDataModel model1 = new FileDataModel(file1);
		
		/*
		 * Para construir o recommender vou aproveitar o metodo builderRecommender da classe
		 * RecomendadorBuilder.
		 */
		
		
		UserBasedRecommender recommender1 = (UserBasedRecommender)builder.buildRecommender(model1);
		
		
		System.out.println("");
		System.out.println("USANDO DADOS REAIS----------------------------------------------------------");
		

		List<RecommendedItem> recommendations1 = recommender1.recommend(2,5);
		
		//Farei um for each para  imprimir as recomendations feitas pelo recommender.
		for(RecommendedItem recommendation: recommendations1) {
			System.out.println(recommendation);
		}
		
		//Agora separei em 60% para treino e 40% para teste.
		double erro1 = evaluator.evaluate(builder, null, model1, 0.6, 0.4);
		System.out.println(erro1);
		
		
		/*USER' QUE NAO AVALIARAM NEHUNHM ITEM, COMO TRATAR
		 * 
		 * Existem casos de user's que recem chegaram no sistema, como recomendar itens para eles, ja que 
		 * eles ainda nao avaliaram nada, ou seja, nao a informacoes sobre eles?
		 * Existem tecnicas, uma delas e fazer perguntas para ele. Vamos supor que um user x acabou de 
		 * se cadastrar. No ato do cadastro, pergunto para se ele quer receber newsletters de uma classe 
		 * de itens y. Como represento a resposta dele em forma de vetor no meus dados?
		 * Simples, voce cria um item que representa essa pergunta e uma nota que representa resposta.
		 * Por exemplo, eu tenho 1000 itens. Crio um item 1001, para uma pergunta e a nota desse user
		 * para esse item seria 1 para sim e 0 para nao. Assim, eu posso encontrar user's mais antigos,
		 * os quais eu ja tenho informacao, que tambem responderam essa pergunta, vejo quais responderam
		 * igual ao novo user, que deram a mesma nota para o item pergunta, e recomendo a partir dessa 
		 * similaridade.
		 */
		
		/*
		 * OBS: Perceba que agora estamos trabalhando com dados fora de escala, isso vai deturpa um pouco 
		 * as recomendacoes. O lance aqui e uma escolha. Por exemplo, se eu trabalho com as notas das 
		 * perguntas em 0 e 1, elas terao sempre nota baixa e nao serao um item que sera recomendado.
		 * Se eu trabalhar com 0 e 10, elas serao recomendadas mas a taxa de erro sera maior, entre
		 * outros problemas. Eu ainda nao sei se existe uma forma de reescalar essas features, 
		 * espero que haja.
		 * 
		 */
		
		/*
		 * 		Essa introducao a classificao nao aborda varias coisas a respeito desse assunto. 
		 * Como por exemplos, a ordem em que as notas sao dadas. As vezes isso e importante,
		 * as vezes nao. Tudo depende. Sim, os arquivos com os dados deveriam estar na pasta 
		 * src/resources, foi mal :).
		 * 
		 *		Continue se aventurando no Machine Learning. Bons estudos!
		 */
		
	}

}
