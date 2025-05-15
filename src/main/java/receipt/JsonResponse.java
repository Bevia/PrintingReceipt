package receipt;

public class JsonResponse {
    private Data data;
    private boolean success;

    // Getters and Setters
    public Data getData() { return data; }
    public void setData(Data data) { this.data = data; }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
}
