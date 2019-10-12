package nl.bsoft.mybatch.config;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Service;

@Service
//@NoArgsConstructor
public
class StepListerConfig {

    @Value("${step.lister.before}")
    private boolean logBeforeStep;

    public StepListerConfig() {
    }

    //    private static StepListerConfig instance = null;
/*
    private StepListerConfig() {

    }

    public static StepListerConfig getInstance() {
        if (instance == null) {
            instance = new StepListerConfig();
        }
        return instance;
    }
*/
    public boolean isLogBeforeStep() {
        return logBeforeStep;
    }

    public void setLogBeforeStep(boolean logBeforeStep) {
        this.logBeforeStep = logBeforeStep;
    }
}
