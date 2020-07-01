package com.exercise.MeetingScheduler.model;

import com.exercise.MeetingScheduler.DateHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.DateSerializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Document(indexName = "reservations")
public class Reservation {
    @Id
    private int reservationId;
    private int schedulingRoomId;

    @NotNull(message = "cannot schedule a meeting without attended managers")
    @Size(min = 2, message = "there should be at least 2 attending managers")
    @Field(type = FieldType.Nested, includeInParent = true)
    private List<Manager> attendedManagers;
    @NotNull(message = "start time meeting time is mandatory")
    @JsonDeserialize(using = DateHandler.class)
    private Date dateBegin;
    @NotNull(message = "end time meeting time is mandatory")
    @JsonDeserialize(using = DateHandler.class)
    private Date dateEnd;


    public Reservation(int reservationId, int schedulingRoomId, Date dateBegin, Date dateEnd, List<Manager> attendedManagers) {
        this.reservationId = reservationId;
        this.schedulingRoomId = schedulingRoomId;
        this.dateBegin = dateBegin;
        this.dateEnd = dateEnd;
        this.attendedManagers = attendedManagers;
    }

    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

    public int getSchedulingRoomId() {
        return schedulingRoomId;
    }

    public void setSchedulingRoomId(int schedulingRoomId) {
        this.schedulingRoomId = schedulingRoomId;
    }

    public Date getDateBegin() {
        return dateBegin;
    }

    public void setDateBegin(Date dateBegin) {
        this.dateBegin = dateBegin;
    }

    public Date getDateEnd() {
        return dateEnd;
    }

    public void setDateEnd(Date dateEnd) {
        this.dateEnd = dateEnd;
    }

    public List<Manager> getAttendedManagers() {
        return attendedManagers;
    }

    public void setAttendedManagers(List<Manager> attendedManagers) {
        this.attendedManagers = attendedManagers;
    }
}
