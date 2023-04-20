
#include <advanlys.h>
#include <utility.h>
#include <formatio.h>
#include <ansi_c.h>
#include <cvirte.h>		
#include <userint.h>
#include "lab1.h"

static int panelHandle;

//==============================================================================
// Constants
#define SAMPLE_RATE		0
#define NPOINTS			1

//==============================================================================
// Global variables
int waveInfo[2]; //waveInfo[0] = sampleRate
				 //waveInfo[1] = number of elements
double sampleRate = 0.0;
int npoints = 0;
double *waveData = 0;

int index_max, index_min;
double val_min, val_max;

double mean, variance;
double median;

int main (int argc, char *argv[])
{
	if (InitCVIRTE (0, argv, 0) == 0)
		return -1;	/* out of memory */
	if ((panelHandle = LoadPanel (0, "lab1.uir", PANEL)) < 0)
		return -1;
	DisplayPanel (panelHandle);
	RunUserInterface ();
	DiscardPanel (panelHandle);
	return 0;
}

int CVICALLBACK OnLoadButtonCB (int panel, int control, int event,
							  void *callbackData, int eventData1, int eventData2)
{
	switch (event)
	{
		case EVENT_COMMIT:
			//executa script python pentru conversia unui fisierului .wav in .txt
			//LaunchExecutable("python main.py");
			
			//astept sa fie generate cele doua fisiere (modificati timpul daca este necesar
			Delay(4);
			
			//incarc informatiile privind rata de esantionare si numarul de valori
			FileToArray("wafeInfo.txt", waveInfo, VAL_INTEGER, 2, 1, VAL_GROUPS_TOGETHER, VAL_GROUPS_AS_COLUMNS, VAL_ASCII);
			sampleRate = waveInfo[SAMPLE_RATE];
			npoints = waveInfo[NPOINTS];
			
			//alocare memorie pentru numarul de puncte
			waveData = (double *) calloc(npoints, sizeof(double));
			
			//incarcare din fisierul .txt in memorie (vector)
			FileToArray("waveData.txt", waveData, VAL_DOUBLE, npoints, 1, VAL_GROUPS_TOGETHER, VAL_GROUPS_AS_COLUMNS, VAL_ASCII);
			
			//afisare pe grapf
			PlotY(panel, PANEL_GRAPH, waveData, npoints, VAL_DOUBLE, VAL_THIN_LINE, VAL_EMPTY_SQUARE, VAL_SOLID, VAL_CONNECTED_POINTS, VAL_RED);
			
			// calcul valori si index min/max 
			MaxMin1D(waveData, npoints, &val_max, &index_max, &val_min, &index_min);
			
			// media si dispersie
			Variance (waveData, npoints, &mean, &variance);
			
			// mediana
			Median (waveData, npoints, &median);
			
			// nr. de treceri prin zero
			int countZeroPasses = 0;
			
			for(int i=0; i<npoints-1; i++){
				if((waveData[i] <=0 && waveData[i+1]>=0) || (waveData[i]>=0 && waveData[i+1]<=0) )
					countZeroPasses++;
				}
			
			printf("val_min = %lf, val_max = %lf \nmedia = %lf, dispersia = %lf \nmediana = %lf \nnr_treceri_prin_zero = %d", val_min, val_max, mean, variance, median, countZeroPasses);
			
			break;
	}
	return 0;
}

int CVICALLBACK OnPanel (int panel, int event, void *callbackData,
							   int eventData1, int eventData2)
{
	switch (event)
	{
		case EVENT_GOT_FOCUS:

			break;
		case EVENT_LOST_FOCUS:

			break;
		case EVENT_CLOSE:
			exit(0);
	}
	return 0;
}
