package shodrone.core.domain.showproposal;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShowProposalVideoTest {
    @Test
    void ensureValidVideoUrlCanBeCreated() {
        String validUrl = "https://example.com/video.mp4";
        ShowProposalVideo video = new ShowProposalVideo(validUrl);
        assertEquals(validUrl, video.getVideoUrl());
    }

    @Test
    void ensureInvalidVideoUrlThrowsException() {
        String invalidUrl = "invalid-url";
        assertThrows(IllegalArgumentException.class, () -> new ShowProposalVideo(invalidUrl));
    }

    @Test
    void ensureNullVideoUrlThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new ShowProposalVideo(null));
    }

    @Test
    void ensureExtensionlessVideoUrlThrowsException() {
        String noExtensionUrl = "www.example";
        assertThrows(IllegalArgumentException.class, () -> new ShowProposalVideo(noExtensionUrl));
    }

    @Test
    void ensureEmptyVideoUrlThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new ShowProposalVideo(""));
    }

    @Test
    void ensureDomainlessVideoUrlThrowsException() {
        String domainlessUrl = "https://";
        assertThrows(IllegalArgumentException.class, () -> new ShowProposalVideo(domainlessUrl));
    }
}