import se.umu.cs.dv21cgn.radioinfo.api.RadioApi;
import se.umu.cs.dv21cgn.radioinfo.api.sr.SrRadioApi;
import se.umu.cs.dv21cgn.radioinfo.api.sr.SrRadioApiScheduleItemResponse;
import se.umu.cs.dv21cgn.radioinfo.api.sr.SrRadioApiScheduleProgramItem;
import se.umu.cs.dv21cgn.radioinfo.api.sr.SrRadioApiScheduleResponse;
import se.umu.cs.dv21cgn.radioinfo.model.HttpBadRequestException;
import se.umu.cs.dv21cgn.radioinfo.controller.RadioInfoController;
import se.umu.cs.dv21cgn.radioinfo.model.*;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URISyntaxException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ControllerTests {
    @Test
    void programStartTimeIsMoreThan12HoursBefore() {
        Program program = new Program(1, "test", "test", LocalDateTime.now().minusHours(13), LocalDateTime.now(), null);
        Schedule schedule = new Schedule();
        schedule.add(program);
        assertFalse(schedule.getPrograms().contains(program));
    }

    @Test
    void programStartTimeIsMoreThan6HoursAfter() {
        Program program = new Program(1, "test", "test", LocalDateTime.now().plusHours(7), LocalDateTime.now(),null);
        Schedule schedule = new Schedule();
        schedule.add(program);
        assertFalse(schedule.getPrograms().contains(program));
    }

    @Test
    void programStartTimeBetween12BeforeAnd6After() {
        Program program = new Program(1, "test", "test", LocalDateTime.now(), LocalDateTime.now(), null);
        Schedule schedule = new Schedule();
        schedule.add(program);
        assertTrue(schedule.getPrograms().contains(program));
    }

    @Test
    void shouldNotAddProgramsOnExceptionThrown() {
        RadioApi radioApi = mock(RadioApi.class);
        RadioInfoModel radioInfoModel = new RadioInfoModel();
        RadioInfoController radioInfoController = spy(new RadioInfoController(radioInfoModel, radioApi));
        int channelId = 132; //Channel id for P1
        LinkedHashMap<Integer, Channel> channelLinkedHashMap = radioInfoModel.getChannels();
        channelLinkedHashMap.put(channelId, new Channel(channelId, "P1", new Schedule()));
        radioInfoModel.setChannels(channelLinkedHashMap);
        try {
            doThrow(HttpBadRequestException.class).when(radioInfoController).fetchSchedule(channelId);
            radioInfoController.fetchSchedule(channelId);
            fail("Didn't throw exception");
        } catch (HttpBadRequestException | IOException | URISyntaxException | InterruptedException e) {
            assertTrue(radioInfoModel.getChannels().get(channelId).schedule().getPrograms().isEmpty());
        }
    }
    @Test
    void shouldFetchAllChannels() throws HttpBadRequestException, IOException, URISyntaxException, InterruptedException {
        RadioInfoModel radioInfoModel = new RadioInfoModel();
        RadioInfoController channelController = new RadioInfoController(radioInfoModel, new SrRadioApi());
        channelController.fetchChannels();
        Channel channel = channelController.getChannel(200);
        assertNotNull(channelController.getChannels());
        assertEquals("P4 Jämtland", channel.name());
    }

    @Test
    void shouldUpdateScheduleForChannelP4Jämtland() throws HttpBadRequestException, IOException, URISyntaxException, InterruptedException {
        LinkedHashMap<Integer, Channel> channelHashMap = new LinkedHashMap<>();
        channelHashMap.put(200, new Channel(200, "P4 Jämtland", new Schedule()));
        RadioInfoController radioInfoController = new RadioInfoController(new RadioInfoModel(channelHashMap), new SrRadioApi());
        radioInfoController.fetchSchedule(200);
        assertFalse(radioInfoController.scheduleIsEmpty(200));
    }
    @Test
    void shouldUpdateCachedChannelsSchedules() throws HttpBadRequestException, IOException, URISyntaxException, InterruptedException {
        SrRadioApi radioApi = mock(SrRadioApi.class);
        LinkedHashMap<Integer, Channel> channels = new LinkedHashMap<>();
        Schedule schedule = new Schedule();
        schedule.add(new Program(1, "test", "test", LocalDateTime.now(), LocalDateTime.now(), null));
        channels.put(200, new Channel(200,"P4 Jämtland", schedule));
        channels.put(132, new Channel(132,"P1", schedule));

        SrRadioApiScheduleItemResponse updatedItem = new SrRadioApiScheduleItemResponse(10, "updated", "updated", "/Date(" + LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli() + ")/", "/Date(" + LocalDateTime.now().toInstant(ZoneOffset.UTC).toEpochMilli() + ")/", new SrRadioApiScheduleProgramItem(1, "lol"), "Image" );
        ArrayList<SrRadioApiScheduleItemResponse> updatedSchedule = new ArrayList<>();
        updatedSchedule.add(updatedItem);
        when(radioApi.getSchedule(anyInt())).thenReturn(new SrRadioApiScheduleResponse(updatedSchedule));

        RadioInfoController radioInfoController = new RadioInfoController(new RadioInfoModel(channels), radioApi);
        radioInfoController.updateCachedSchedules();

        verify(radioApi, times(2)).getSchedule(anyInt());
    }

    @Test
    void shouldFetchScheduleOnChannelWithEmptySchedule() throws HttpBadRequestException, IOException, URISyntaxException, InterruptedException {
        SrRadioApi radioApi = mock(SrRadioApi.class);
        LinkedHashMap<Integer, Channel> channels = new LinkedHashMap<>();
        channels.put(200, new Channel(200,"P4 Jämtland", new Schedule()));
        RadioInfoModel radioInfoModel = new RadioInfoModel(channels);
        RadioInfoController radioInfoController = new RadioInfoController(radioInfoModel, radioApi);
        when(radioApi.getSchedule(200)).thenReturn(new SrRadioApiScheduleResponse(new ArrayList<>()));
        radioInfoController.selectChannel(200);

        verify(radioApi, times(1)).getSchedule(200);
    }

    @Test
    void shouldKeepConcurrentState() throws HttpBadRequestException, IOException, URISyntaxException, InterruptedException {
        for (int i = 0; i < 10; i++) {
            SrRadioApi srRadioApi = new SrRadioApi();
            RadioInfoModel model = new RadioInfoModel();
            RadioInfoController controller = new RadioInfoController(model, srRadioApi);
            controller.fetchChannels();
            controller.selectChannel(132);
            Thread t1 = new Thread(() -> {
                try {
                    controller.selectChannel(200);
                } catch (HttpBadRequestException | IOException | URISyntaxException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });
            Thread t2 = new Thread(() -> {
                try {
                    controller.updateCachedSchedules();
                } catch (HttpBadRequestException | IOException | URISyntaxException | InterruptedException e) {
                    throw new RuntimeException(e);
                }
            });

            t1.start();
            t2.start();

            t1.join();
            assertFalse(controller.scheduleIsEmpty(200));
            t2.join();
            assertFalse(controller.scheduleIsEmpty(200));

        }
    }

}
