package org.continuouspoker.dealer.game;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.continuouspoker.dealer.LogEntry;
import org.continuouspoker.dealer.Team;

@Slf4j
@RequiredArgsConstructor
public class Game implements Runnable {

    private final List<Team> teams = new ArrayList<>();

    @Getter
    @JsonIgnore
    private final List<Tournament> tournaments = new ArrayList<>();

    @Getter
    private final long gameId;
    @Getter
    private final String name;
    private final Duration timeBetweenGameRounds;
    private final Duration timeBetweenSteps;

    @Getter
    @JsonIgnore
    private int tournamentId;

    @Override
    public void run() {

        final Tournament tournament = new Tournament(gameId, tournamentId++, teams, timeBetweenGameRounds,
                timeBetweenSteps);
        tournaments.add(tournament);
        tournament.run();
    }

    @JsonIgnore
    public Stream<LogEntry> getFullHistory() {
        return tournaments.stream().flatMap(Tournament::getHistory);
    }

    public void addPlayer(final Team team) {
        teams.add(team);
    }

    public void removePlayer(final Team team) {
        teams.remove(team);
    }

    public List<Team> getTeams() {
        return Collections.unmodifiableList(teams);
    }
}
