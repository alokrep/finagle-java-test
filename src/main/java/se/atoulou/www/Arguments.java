package se.atoulou.www;

import com.beust.jcommander.Parameter;

public class Arguments {
    @Parameter(names = { "-P", "--port" }, description = "Port")
    private Integer port = 8080;

    public Integer getPort() {
        return port;
    }

}