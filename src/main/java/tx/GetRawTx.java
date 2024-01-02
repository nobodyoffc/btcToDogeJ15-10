package tx;

public class GetRawTx {
    private String params;
    private String result;

    public static String method = "getrawtransaction";

    public String getRawTx(String txId, String url, String username, String password){
        RpcRequest jsonRPC2Request = new RpcRequest(method,new Object[]{txId});
        Object result = RpcRequest.requestRpc(url, username,password,method,jsonRPC2Request);
        return (String)result;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getResult() {
        return result;
    }
    public void setResult(String result) {
        this.result = result;
    }
}
