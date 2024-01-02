package tx;

import com.google.gson.Gson;

public class EstimateFee {
    private Object[] params;
    private double result;
    private ResultSmart resultSmart;

    public static String ESTIMATE_FEE = "estimatefee";
    public static String ESTIMATE_SMART_FEE = "estimatesmartfee";

    public double estimatefee(String url, String username, String password){

        RpcRequest jsonRPC2Request = new RpcRequest(ESTIMATE_FEE, null);

        result = (double) RpcRequest.requestRpc(url, username,password,ESTIMATE_FEE,jsonRPC2Request);
        return result;
    }
    public double estimatefee(int nBlocks, String url, String username, String password){
        params = new Object[]{nBlocks};
        RpcRequest jsonRPC2Request = new RpcRequest(ESTIMATE_FEE, params);

        result = (double) RpcRequest.requestRpc(url, username,password,ESTIMATE_FEE,jsonRPC2Request);
        return result;
    }
    public ResultSmart estimatesmartfee(int nBlocks, String url, String username, String password){
        params = new Object[]{nBlocks};
        RpcRequest jsonRPC2Request = new RpcRequest(ESTIMATE_SMART_FEE, params);

        Object result1 = RpcRequest.requestRpc(url, username, password, ESTIMATE_SMART_FEE, jsonRPC2Request);
        Gson gson = new Gson();
        resultSmart = gson.fromJson(gson.toJson(result1),ResultSmart.class);
        return resultSmart;
    }
    public class ResultSmart{
        private double feerate;
        private long blocks;

        public double getFeerate() {
            return feerate;
        }

        public void setFeerate(double feerate) {
            this.feerate = feerate;
        }

        public long getBlocks() {
            return blocks;
        }

        public void setBlocks(long blocks) {
            this.blocks = blocks;
        }
    }

    public Object[] getParams() {
        return params;
    }

    public void setParams(Object[] params) {
        this.params = params;
    }

    public double getResult() {
        return result;
    }

    public void setResult(double result) {
        this.result = result;
    }
}
