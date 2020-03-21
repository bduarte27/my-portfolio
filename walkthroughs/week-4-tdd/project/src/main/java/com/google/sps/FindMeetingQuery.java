// Copyright 2019 Google LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     https://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.sps;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public final class FindMeetingQuery {
  private static final boolean INCLUSIVE = true;

  public Collection<TimeRange> query(Collection<Event> events, MeetingRequest request) {
    ArrayList<TimeRange> availableTimes = new ArrayList();
    if (request.getDuration() > TimeRange.WHOLE_DAY.duration()) {
      return availableTimes;
    }

    // Made copy of events as List because of trouble utilizing Collection data type
    List<Event> eventsCopy = copyEventsIntoSortedList(events);
    int prev_endTime = TimeRange.START_OF_DAY;
    for (Event event : eventsCopy) {
      if (anyAttendeeIsUnavailable(event, request)) {
        updateAvailableTimes(availableTimes, prev_endTime, event.getWhen().start(), !INCLUSIVE);
        prev_endTime = getLatestEndTime(prev_endTime, event.getWhen().end());
      }
    }

    updateAvailableTimes(availableTimes, prev_endTime, TimeRange.END_OF_DAY, INCLUSIVE);
    return availableTimes;
  }

  private List<Event> copyEventsIntoSortedList(Collection<Event> events) {
    List<Event> eventsCopy = new ArrayList<Event>(events);
    eventsCopy.sort(Comparator.comparing(Event::getWhen, TimeRange.ORDER_BY_START));
    return eventsCopy;
  }

  private boolean anyAttendeeIsUnavailable(Event event, MeetingRequest request) {
    return !Collections.disjoint(event.getAttendees(), request.getAttendees());
  }

  private void updateAvailableTimes(ArrayList<TimeRange> availableTimes, int prev_endTime,
      int currentEndTime, boolean isInclusive) {
    if (existTimeAvailableInBetween(prev_endTime, currentEndTime)) {
      addTimeToAvailableTimes(availableTimes, prev_endTime, currentEndTime, isInclusive);
    }
  }

  private boolean existTimeAvailableInBetween(int prev_endTime, int currentEndTime) {
    return prev_endTime < currentEndTime;
  }

  private void addTimeToAvailableTimes(
      ArrayList<TimeRange> availableTimes, int newTimeStart, int newTimeEnd, boolean isInclusive) {
    availableTimes.add(TimeRange.fromStartEnd(newTimeStart, newTimeEnd, isInclusive));
  }

  private int getLatestEndTime(int prev_endTime, int currentEndTime) {
    return Math.max(prev_endTime, currentEndTime);
  }
}