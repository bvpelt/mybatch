package nl.bsoft.mybatch.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public @Data
class StepListenerConfig {

    @Value("${mybatch.log.beforeStep:false}")
    private boolean logBeforeStep;

    @Value("${mybatch.log.beforeChunk:false}")
    private boolean logBeforeChunk;

    @Value("${mybatch.log.beforeRead:false}")
    private boolean logBeforeRead;

    @Value("${mybatch.log.afterRead:false}")
    private boolean logAfterRead;

    @Value("${mybatch.log.ReadError:true}")
    private boolean logReadError;

    @Value("${mybatch.log.SkipInRead:true}")
    private boolean logSkipInRead;

    @Value("${mybatch.log.BeforeProcess:false}")
    private boolean logBeforeProcess;

    @Value("${mybatch.log.AfterProcess:false}")
    private boolean logAfterProcess;

    @Value("${mybatch.log.ProcessError:true}")
    private boolean logProcessError;

    @Value("${mybatch.log.SkipInProcess:true}")
    private boolean logSkipInProcess;

    @Value("${mybatch.log.BeforeWrite:false}")
    private boolean logBeforeWrite;

    @Value("${mybatch.log.AfterWrite:false}")
    private boolean logAfterWrite;

    @Value("${mybatch.log.WriteError:true}")
    private boolean logWriteError;

    @Value("${mybatch.log.SkipInWrite:false}")
    private boolean logSkipInWrite;

    @Value("${mybatch.log.AfterChunk:false}")
    private boolean logAfterChunk;

    @Value("${mybatch.log.ChunkError:true}")
    private boolean logChunkError;

    @Value("${mybatch.log.AfterStep:false}")
    private boolean logAfterStep;

}
