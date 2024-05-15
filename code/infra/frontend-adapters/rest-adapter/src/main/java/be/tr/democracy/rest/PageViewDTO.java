package be.tr.democracy.rest;

import java.util.List;

public record PageViewDTO<T>(int pageNr,
                             int pageSize,
                             int totalPages,
                             List<T> values
) {
}
/**
 * export interface PartyVotes {
 *   partyName: string;
 *   votePercentage: number;
 * }
 *
 * export interface Page<T> {
 *   pageNr: number;
 *   pageSize: number;
 *   totalPages: number;
 *   values: T[];
 * }
 */
