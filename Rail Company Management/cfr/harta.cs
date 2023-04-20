using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Data;
using System.Drawing;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using System.Windows.Forms;
//using System.Windows.Forms.Resolution;

namespace cfr
{
    public partial class harta : Form
    {
        public harta()
        {
            InitializeComponent();
        }

        private void harta_Load(object sender, EventArgs e)
        {
            pictureBox1.BackgroundImage = Image.FromFile(@"cfr.png");
            pictureBox1.BackgroundImageLayout = ImageLayout.Stretch;
        }

    }
}
