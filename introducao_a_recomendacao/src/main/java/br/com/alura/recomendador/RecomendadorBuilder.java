package br.com.alura.recomendador;

import org.apache.mahout.cf.taste.common.TasteException;
import org.apache.mahout.cf.taste.eval.RecommenderBuilder;
import org.apache.mahout.cf.taste.impl.neighborhood.ThresholdUserNeighborhood;
import org.apache.mahout.cf.taste.impl.recommender.GenericUserBasedRecommender;
import org.apache.mahout.cf.taste.impl.similarity.PearsonCorrelationSimilarity;
import org.apache.mahout.cf.taste.model.DataModel;
import org.apache.mahout.cf.taste.neighborhood.UserNeighborhood;
import org.apache.mahout.cf.taste.recommender.Recommender;
import org.apache.mahout.cf.taste.recommender.UserBasedRecommender;
import org.apache.mahout.cf.taste.similarity.UserSimilarity;

public class RecomendadorBuilder implements RecommenderBuilder{

	public Recommender buildRecommender(DataModel model) throws TasteException {
	
			
	
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
		
		
		return recommender;
	}

}
