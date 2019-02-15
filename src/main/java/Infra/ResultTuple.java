package Infra;

import java.util.ArrayList;

public class ResultTuple {

    ArrayList<DataNode> match;
    DataNode root;
    int[] eids;

    public ResultTuple (ArrayList<DataNode> match, DataNode root, int k) {
        this.match = match;
        this.root = root;
        this.eids = new int[k];
    }


}
