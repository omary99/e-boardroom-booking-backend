package com.boardroom.boardroom_booking.repository;

import com.boardroom.boardroom_booking.model.Booking;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {
    List<Booking> findBookingByDate(LocalDate date);

    List<Booking> findBookingByStartTime(LocalTime date);

    List<Booking> findBookingByEndTime(LocalTime date);

    boolean existsByRoomNameAndDateAndStartTimeLessThanEqualAndEndTimeGreaterThanEqual(
            String roomName,
            LocalDate date,
            LocalTime startTime,
            LocalTime endTime
    );

    List<Booking> findByDepartmentId(Long departmentId);

    List<Booking> findByUserId(Long userId);

    @Query("""
                SELECT COUNT(b)
                FROM Booking b
                WHERE b.date = :date
                AND b.status <> 'CANCELLED'
            """)
    long countByDateExcludingCancelled(@Param("date") LocalDate date);

    @Query("""
                SELECT COUNT(b) 
                FROM Booking b 
                WHERE b.date = CURRENT_DATE 
                AND b.startTime <= CURRENT_TIME 
                AND b.endTime >= CURRENT_TIME
                AND b.status <> 'CANCELLED'
            """)
    long countOngoingMeetings();

    @Query("""
                SELECT COUNT(b) 
                FROM Booking b 
                WHERE b.user.id = :userId 
                AND b.date > CURRENT_DATE
                AND b.status <> 'CANCELLED'
            """)
    long countMyUpcomingBookings(@Param("userId") Long userId);

    @Query("""
                SELECT DISTINCT b.roomName 
                FROM Booking b 
                WHERE b.date = CURRENT_DATE
                AND b.status <> 'CANCELLED'
            """)
    List<String> findBookedRoomsToday();

    @Query("""
                SELECT COUNT(b)
                FROM Booking b
                WHERE b.date = :tomorrow
                AND b.status <> 'CANCELLED'
            """)
    long countTomorrowMeetings(@Param("tomorrow") LocalDate tomorrow);

    @Query("""
    SELECT COUNT(b)
    FROM Booking b
    WHERE b.date = CURRENT_DATE
    AND b.status = 'CANCELLED'
""")
    long countCancelledBookingsToday();

}
