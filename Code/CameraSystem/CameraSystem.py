import cv2 as cv
import os

face_classifier = cv.CascadeClassifier(cv.data.haarcascades + "haarcascade_frontalface_default.xml")

capture = cv.VideoCapture(0)



def detect_bounding_box(video):
    gray_image = cv.cvtColor(video, cv.COLOR_BGR2GRAY)
    face = face_classifier.detectMultiScale(gray_image, 1.1, 5, minSize = (40, 40))
    for (x, y, w, h) in face:
        cv.rectangle(video, (x, y), (x + w, y + h), (0, 255, 0), 4)
    return face

while cv.VideoCapture.isOpened:
    
    ret, frame = capture.read()
    
    faces = detect_bounding_box(frame)
    cv.imshow("Facial Detection", frame)

    if cv.waitKey(1) & 0xFF == ord("q"):
        break

capture.release()
cv.destroyAllWindows()




