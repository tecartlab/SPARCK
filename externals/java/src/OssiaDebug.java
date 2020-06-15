
import com.cycling74.max.*;

public class OssiaDebug extends MaxObject{

	public OssiaDebug(Atom args[]) {
		declareInlets(new int[] { DataTypes.ALL });
		declareOutlets(new int[] { DataTypes.ALL, DataTypes.ALL, DataTypes.ALL, DataTypes.ALL, DataTypes.ALL, DataTypes.ALL, DataTypes.ALL});
	}

	public void bang(){
	}

}
