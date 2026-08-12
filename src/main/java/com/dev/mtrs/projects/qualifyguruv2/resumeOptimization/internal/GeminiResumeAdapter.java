package com.dev.mtrs.projects.qualifyguruv2.resumeOptimization.internal;

import com.dev.mtrs.projects.qualifyguruv2.resumeOptimization.AdaptedResumeResponse;
import com.dev.mtrs.projects.qualifyguruv2.resumeOptimization.JobDescriptionRequest;
import com.dev.mtrs.projects.qualifyguruv2.resumeOptimization.ResumeOptimizationPort;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class GeminiResumeAdapter implements ResumeOptimizationPort {

    private final ChatClient chatClient;

    public GeminiResumeAdapter(ChatClient.Builder builder) {
        this.chatClient = builder
                .defaultSystem("Você é um sistema especialista em otimização de currículos (ATS). " +
                        "REGRA ABSOLUTA: Você É ESTRITAMENTE PROIBIDO de inventar ou adicionar " +
                        "habilidades, experiências ou métricas que não existam no currículo original.")
                .build();
    }

    @Override
    public AdaptedResumeResponse adaptResume(String rawResumeText, JobDescriptionRequest jobDescription) {

        String promptTemplate = """
            Adapte o currículo do candidato para a vaga descrita, destacando a compatibilidade.
            Retorne ESTRITAMENTE os dados solicitados, sem adicionar explicações fora do formato.
            
            VAGA ALVO:
            Título: {jobTitle}
            Descrição: {jobDescription}
            
            CURRÍCULO ORIGINAL:
            {resumeText}
            """;

        return chatClient.prompt()
                .user(u -> u.text(promptTemplate)
                        .params(Map.of(
                                "jobTitle", jobDescription.title(),
                                "jobDescription", jobDescription.description(),
                                "resumeText", rawResumeText
                        )))

                .call()
                .entity(AdaptedResumeResponse.class);
    }
}
