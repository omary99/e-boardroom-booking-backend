package com.boardroom.boardroom_booking.DTO;

public class DashboardResponse {
    private long ongoingMeetings;
    private long tomorrowMeetings;
    private long cancelledBookingsToday;
    private long myUpcomingBookings;

    public DashboardResponse() {
    }

    public DashboardResponse(long ongoingMeetings, long tomorrowMeetings, long cancelledBookingsToday, long myUpcomingBookings) {
        this.ongoingMeetings = ongoingMeetings;
        this.tomorrowMeetings = tomorrowMeetings;
        this.cancelledBookingsToday = cancelledBookingsToday;
        this.myUpcomingBookings = myUpcomingBookings;
    }

    public long getOngoingMeetings() {
        return ongoingMeetings;
    }

    public void setOngoingMeetings(long ongoingMeetings) {
        this.ongoingMeetings = ongoingMeetings;
    }

    public long getTomorrowMeetings() {
        return tomorrowMeetings;
    }

    public void setTomorrowMeetings(long tomorrowMeetings) {
        this.tomorrowMeetings = tomorrowMeetings;
    }

    public long getCancelledBookingsToday() {
        return cancelledBookingsToday;
    }

    public void setCancelledBookingsToday(long cancelledBookingsToday) {
        this.cancelledBookingsToday = cancelledBookingsToday;
    }

    public long getMyUpcomingBookings() {
        return myUpcomingBookings;
    }

    public void setMyUpcomingBookings(long myUpcomingBookings) {
        this.myUpcomingBookings = myUpcomingBookings;
    }
}
