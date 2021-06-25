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
      meetings: [],
    };
  },

  methods: {
    addNewMeeting(meeting) {
      this.meetings.push(meeting);
    },

    addMeetingParticipant(meeting) {
      this.$http
        .put(
          `meetings/${meeting.id}/${this.username}`,
          {},
          Vue.http.headers.common.Authorization
        )
        .then(() => this.loadMeetings())
        .catch((response) => {
          console.log("Błąd dodawania użytkownika", response.status);
        });
    },

    removeMeetingParticipant(meeting) {
      this.$http
        .delete(
          `meetings/${meeting.id}/${this.username}`,
          {},
          Vue.http.headers.common.Authorization
        )
        .then(() => this.loadMeetings())
        .catch((response) => {
          console.log("Błąd usuwania użytkownika", response.status);
        });
    },

    deleteMeeting(meeting) {
      this.$http
        .delete(
          `meetings/${meeting.id}`,
          {},
          Vue.http.headers.common.Authorization
        )
        .then(() => this.loadMeetings())
        .catch((response) => {
          console.log("Błąd usuwania spotkania", response.status);
        });
    },

    loadMeetings() {
      this.$http
        .get("meetings", {}, Vue.http.headers.common.Authorization)
        .then((response) => {
          this.meetings = response.body;
        })
        .catch((response) => {
          console.log("Błąd pobierania listy spotkań", response.status);
        });
    },
  },

  beforeMount() {
    this.loadMeetings();
  },
};
</script>
