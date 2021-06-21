<template>
  <div>
    <new-meeting-form @added="addNewMeeting($event)"></new-meeting-form>

    <span v-if="meetings.length == 0"> Brak zaplanowanych spotkań. </span>
    <h3 v-else>Zaplanowane zajęcia ({{ meetings.length }})</h3>

    <meetings-list
      :meetings="meetings"
      :username="username"
      @attend="addMeetingParticipant($event)"
      @unattend="removeMeetingParticipant($event)"
      @delete="deleteMeeting($event)"
    ></meetings-list>
  </div>
</template>

<script>
import NewMeetingForm from "./NewMeetingForm";
import MeetingsList from "./MeetingsList";
import Vue from "vue";

export default {
  components: { NewMeetingForm, MeetingsList },
  props: ["username"],
  data() {
    return {
      meetings: () => {
        methods.loadMeetings();
        return [];
      },
    };
  },

  methods: {
    addNewMeeting(meeting) {
      this.meetings.push(meeting);
    },
    addMeetingParticipant(meeting) {
      meeting.participants.push(this.username);
    },
    removeMeetingParticipant(meeting) {
      this.$http
        .delete(
          `participants/${this.username}`,
          {},
          Vue.http.headers.common.Authorization
        )
        .then((response) => {
          this.meetings = response.body;
        });

      //   meeting.participants.splice(
      //     meeting.participants.indexOf(this.username),
      //     1
      //   );
    },
    deleteMeeting(meeting) {
      this.$http
        .delete(
          `meetings/${meeting.id}/${this.username}`,
          {},
          Vue.http.headers.common.Authorization
        )
        .then((response) => {
          this.meetings = response.body;
        });
      //   this.meetings.splice(this.meetings.indexOf(meeting), 1);
    },
    loadMeetings() {
      this.$http
        .get("meetings", {}, Vue.http.headers.common.Authorization)
        .then((response) => {
          this.meetings = response.body;
        });
    },
  },
  beforeMount() {
    this.loadMeetings();
  },
};
</script>
