package tx;


public class SendRawTransaction {
    private String[] params;
    private String result;

    public static String method = "sendrawtransaction";


    public String sendRawTransaction(String hex, String url,String username,String password){
        String[] params = new String[]{hex};
        RpcRequest jsonRPC2Request = new RpcRequest(method, params);

        JsonTools.gsonPrint(jsonRPC2Request);

        Object result = RpcRequest.requestRpc(url, username,password,"sendRawTransaction",jsonRPC2Request);
        return (String)result;
    }

    public String[] getParams() {
        return params;
    }

    public void setParams(String[] params) {
        this.params = params;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
