package org.vayanlabs;

import com.google.adk.agents.*;
import com.google.adk.artifacts.BaseArtifactService;
import com.google.adk.artifacts.InMemoryArtifactService;
import com.google.adk.events.Event;
import com.google.adk.runner.Runner;
import com.google.adk.sessions.BaseSessionService;
import com.google.adk.sessions.InMemorySessionService;
import com.google.adk.sessions.Session;
import com.google.genai.types.Content;
import com.google.genai.types.Part;
import io.reactivex.rxjava3.core.Flowable;
import io.reactivex.rxjava3.core.Single;

import java.util.Locale;
import java.util.UUID;

class InstructionFactory {
    private final Integer someContext = 5;

    private Single<String> constructInstructionFromContext(ReadonlyContext rdCtx) {
        String instruction;
        // instruction can be generated dynamically depending on class elements and readonlycontext
        if (this.someContext == 5 &&
                rdCtx.equals(rdCtx)) {
            instruction = "You are a hello world agent.";
        } else {
            instruction = "I don't know what agent you are, just say you can't give any more information.";
        }
        return Single.fromCallable(() -> instruction);
    }

    public Instruction getDynamicInstruction() {
        return new Instruction.Provider(this::constructInstructionFromContext);
    }
}

class InMemoryRunner {
    private String userId;
    private Session session;
    private BaseAgent baseAgent;
    private BaseArtifactService artifactService;
    private BaseSessionService sessionService;
    private Runner runner;
    private final String appName = "vayanlabs";
    private RunConfig runConfig;

    public InMemoryRunner(
            LlmAgent baseAgent
    ) {
        this.baseAgent = (BaseAgent) baseAgent;
        this.artifactService = new InMemoryArtifactService();
        this.sessionService = new InMemorySessionService();
        this.session = this.createSession();
        this.userId = this.createUser();

        this.constructInMemoryRunner();
        this.runConfig = RunConfig.builder().build(); // just default configs
    }

    private void constructInMemoryRunner() {
        this.runner = new Runner(
                this.baseAgent,
                this.appName,
                this.artifactService,
                this.sessionService,
                null
        );
    }

    private String createUser() {
        String userId = this.session.userId();
        System.out.println("Created user: " + userId);
        return userId;
    }

    private Session createSession() {
        String sessionId = UUID.randomUUID().toString();
        Session session =  this.sessionService.createSession(
                appName,
                sessionId
        ).blockingGet(); // can create session in a separate thread as well

        System.out.println("Created session: " + session.id());
        return session;
    }

    private Content createMessage(String userQuery) {
        return Content.fromParts(Part.fromText(userQuery));
    }

    public Flowable<Event> run(String userQuery) {
        System.out.println("USER QUERY: " + userQuery);
        return runner.runAsync(
                this.userId,
                this.session.id(),
                this.createMessage(userQuery),
                this.runConfig);
    }
}

public class Main {
    static void main() {
        InstructionFactory instructionFactory = new InstructionFactory();
        LlmAgent.Builder builder = new LlmAgent.Builder()
                .name("Hello World")
                .description("Hello world agent")
                .model("gemini-2.5-flash")
                .instruction(instructionFactory.getDynamicInstruction());
        LlmAgent agent = builder.build();

        InMemoryRunner runner = new InMemoryRunner(agent);
        runner.run("What are the advantages of AI")
                .subscribe(
                event -> {
                    if(event.finalResponse()) {
                        System.out.println("FINAL RESPONSE: " + event.toString().toUpperCase(Locale.ROOT));
                    }
                    System.out.println(event.toString());
                }
        );
    }
}