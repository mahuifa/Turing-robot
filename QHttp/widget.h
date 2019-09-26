#ifndef WIDGET_H
#define WIDGET_H

#include <QWidget>

#include <QNetworkAccessManager>       //三个http必须头文件
#include <QNetworkReply>
#include <QNetworkRequest>


namespace Ui {
class Widget;
}

class Widget : public QWidget
{
    Q_OBJECT

public:
    explicit Widget(QWidget *parent = nullptr);
    ~Widget();

    QNetworkAccessManager *manager;
    QNetworkReply  *reply;
    QNetworkRequest *requst;

    QJsonObject *JsonData;      //封装的json信息

    QString getJson();          //解析返回的json信息
    void Json_data();           //封装json信息

private slots:
    void doProcessReadyRead();
    void on_pushButton_clicked();
    void doProceesFinished();

private:
    Ui::Widget *ui;
};

#endif // WIDGET_H
