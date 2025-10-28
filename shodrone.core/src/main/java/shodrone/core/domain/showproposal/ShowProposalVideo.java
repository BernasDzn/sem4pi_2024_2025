package shodrone.core.domain.showproposal;

import eapli.framework.domain.model.ValueObject;
import lombok.Getter;

public class ShowProposalVideo implements ValueObject {

    @Getter
    private final String videoUrl;

    public ShowProposalVideo(String videoUrl) {
        if (videoUrl == null || videoUrl.isEmpty()) {
            throw new IllegalArgumentException("Video URL cannot be null or empty");
        }
        if(!videoUrl.matches("^(https?|ftp)://[^\s/$.?#].[^\s]*$")) {
            throw new IllegalArgumentException("Invalid video URL format");
        }
        this.videoUrl = videoUrl;
    }

    @Override
    public boolean equals(Object other) {
        if (!(other instanceof ShowProposalVideo)) {
            return false;
        }
        ShowProposalVideo that = (ShowProposalVideo) other;
        return this.videoUrl.equals(that.videoUrl);
    }

    @Override
    public String toString() {
        return "ShowProposalVideo{" +
                "videoUrl='" + videoUrl + '\'' +
                '}';
    }
}
