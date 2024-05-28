public class Text {
    private String message;
    private int time_length;
    public Text(String message, int time_length){
        this.message = message;
        this.time_length = time_length;
    }

    public String get_text(){
        return message;
    }

    public int get_time_length(){
        return time_length;
    }

}
