package ru.netology.delivery.test;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import lombok.val;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import ru.netology.delivery.data.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

class DeliveryTest {
    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    @Test
    @DisplayName("Should successful plan and replan meeting")
    void shouldChangeDateTest() {
        Configuration.holdBrowserOpen = true;
        val validUser = DataGenerator.Registration.generateUser("ru");
        val daysToAddForFirstMeeting = 4;
        val firstMeetingDate = DataGenerator.generateDate(daysToAddForFirstMeeting);
        val daysToAddForSecondMeeting = 7;
        val secondMeetingDate = DataGenerator.generateDate(daysToAddForSecondMeeting);
        $x("//input[@placeholder='Город']").val(validUser.getCity());
        $x("//input[@type=\"tel\"]").doubleClick().sendKeys(Keys.DELETE);
        $x("//input[@placeholder='Дата встречи']").val(firstMeetingDate);
        $("[data-test-id='name'] input").val(validUser.getName());
        $("[data-test-id='phone'] input").val(validUser.getPhone());
        $("[data-test-id='agreement']").click();
        $x("//*[contains(text(),'Запланировать')]").click();
        $(byText("Успешно!")).shouldBe(Condition.visible, Duration.ofSeconds(15));
        $("[data-test-id='success-notification'] .notification__content").should(exactText("Встреча успешно запланирована на " + firstMeetingDate));
        $x("//input[@type=\"tel\"]").doubleClick().sendKeys(Keys.DELETE);
        $x("//input[@placeholder='Дата встречи']").val(secondMeetingDate);
        $x("//*[contains(text(),'Запланировать')]").click();
        $(byText("Необходимо подтверждение")).shouldBe(Condition.visible, Duration.ofSeconds(15));
        $("[data-test-id='replan-notification'] .notification__content");
        $x("//*[contains(text(),'Перепланировать')]").click();
        $(byText("Успешно!")).shouldBe(Condition.visible, Duration.ofSeconds(15));
        $("[data-test-id='success-notification'] .notification__content").should(exactText("Встреча успешно запланирована на " + secondMeetingDate));

        }
    }

