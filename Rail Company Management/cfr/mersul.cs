using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;

namespace cfr
{
    public partial class mersul : Form
    {
        public mersul()
        {
            InitializeComponent();
        }

        private void mersul_Load(object sender, EventArgs e)
        {
            webBrowser1.Navigate(AppDomain.CurrentDomain.BaseDirectory+@"Mers_2014-2015.pdf");
        }
    }
}
