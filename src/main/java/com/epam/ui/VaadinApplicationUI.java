package com.epam.ui;

import com.epam.domain.CinemaEvent;
import com.vaadin.annotations.Theme;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.time.format.DateTimeFormatter;
import java.util.stream.Collectors;

@Theme("VaadinApplication")
public class VaadinApplicationUI extends UI{

    private ApplicationController controller;

	@Override
	protected void init(VaadinRequest request){
		try {
			ApplicationContext context = new ClassPathXmlApplicationContext("spring.xml");
			controller = (ApplicationController) context.getBean("ApplicationController");
			VerticalLayout verticalLayout = new VerticalLayout();
			Grid<CinemaEvent> grid = buildGrid();
			verticalLayout.addComponents(grid, buildLayout());
			verticalLayout.setComponentAlignment(grid, Alignment.MIDDLE_CENTER);
			setContent(verticalLayout);
		} catch (Throwable throwable) {
			Notification.show(throwable.getMessage());
		}
	}

	private Grid<CinemaEvent> buildGrid(){
	    Grid<CinemaEvent> grid = new Grid<>();
	    grid.setItems(controller.getEvents());
	    grid.addColumn(CinemaEvent::getName).setCaption("Event name");
        grid.addColumn(event -> event.getDates().stream().map(
        		date -> date.format(DateTimeFormatter.ISO_DATE)).collect(Collectors.toSet())).setCaption("Dates");
        grid.addColumn(CinemaEvent::getRating).setCaption("Rating");
        grid.addColumn(CinemaEvent::getPrice).setCaption("Price");
        grid.setSizeFull();
	    return grid;
    }

    private VerticalLayout buildLayout() {
		TextField textField = new TextField("Event name");
		Button button = new Button("Get Event");
		button.addClickListener(event -> controller.getEventByName(textField.getValue()));
		return new VerticalLayout(textField, button);
	}
}
