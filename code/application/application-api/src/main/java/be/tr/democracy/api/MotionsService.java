package be.tr.democracy.api;

import be.tr.democracy.vocabulary.Motion;

import java.util.List;

public interface MotionsService {

    List<Motion> getMotions(int maximum);
}
