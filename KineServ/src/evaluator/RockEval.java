package evaluator;

import java.io.File;

import modele.BayesianTable;
import entagged.audioformats.AudioFile;
import entagged.audioformats.AudioFileIO;
import entagged.audioformats.exceptions.CannotReadException;

public class RockEval implements Evaluator {

	private static final String name = "Rock Evaluator";
	private static int id;
	private static BayesianTable BT;
	
	//Reference values for the typical point
	private final static float REFERENCE_PEAK_VALUE = (float) 0.35;
	private final static float REFERENCE_SPEED_VALUE = (float) 0.80;
	private final static float REFERENCE_LENGTH_VALUE = (float) 0.50;


	@Override
	public float evaluate(File mp3File) {
		float value = 0;
		AudioFile mp3;
		try {
			mp3 = AudioFileIO.read(mp3File);
			String res = StringTag.getStringGenres(mp3);
			System.out.println("GENRE : "+ res);
			if (res.contains("Rock") || 
					res.contains("rock") ) 
			{
				value = 1;
			}
		} catch (CannotReadException e) {
			// TODO Auto-generated catch block
			value = (float) 0.5;
		}
		return value;
	}

	@Override
	public int getId() {
		return id;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public void setId(int id) {
		RockEval.id = id;
		// CREATE THE BAYESIAN TABLE
		BT = new BayesianTable(6,6,6, id);
		for(int i = 0 ; i<(BT.bayesMat.length) ; i++){
			for (int j = 0; j<(BT.bayesMat[0].length); j++ ){
				for (int k = 0; k<(BT.bayesMat[0][0].length); k++){
					BT.bayesMat[i][j][k] = 1 - distanceToRef(i, j , k);
				}
			}
		}
	}
	
	private float distanceToRef(int i, int j, int k){
		float x = ((float) i)/(BT.bayesMat.length-1);
		float y = ((float) j)/(BT.bayesMat[0].length-1);
		float z = ((float) k)/(BT.bayesMat[0][0].length-1);
		float res = 0;
		res+= Math.pow(REFERENCE_PEAK_VALUE - x, 2);
		res+= Math.pow(REFERENCE_SPEED_VALUE - y, 2);
		res+= Math.pow(REFERENCE_LENGTH_VALUE - z, 2);
		res /= 3;//to normalize
		res = (float) Math.sqrt(res);
		return res;
	}

	@Override
	public BayesianTable getBayesTable() {
		return RockEval.BT;
	}

}
